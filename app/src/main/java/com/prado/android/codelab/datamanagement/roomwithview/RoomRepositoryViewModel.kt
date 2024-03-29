package com.prado.android.codelab.datamanagement.roomwithview

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

// +----------------------------------------------------------------------+
// | >>> 1° <<< ENTITY: DEFINICÃO DA NOSSA TABELA POR MEIO DE ANOTACÕES   |
// +----------------------------------------------------------------------+
@Entity(tableName = "word_table")
class Word(
    @PrimaryKey @ColumnInfo(name = "word") val word: String
)

// +-------------------------------------------------------------------------------------+
// | >>> 2° <<< DAO: DATA ACCESS OBJECT - OBJETO QUE REALIZA OPERACÕES NO BANCO DE DADOS |
// |            ESPECIFICA O QUE PODEREMOS ALTERA NA NOSSA TABELA E FACILITA O USO       |
// +-------------------------------------------------------------------------------------+
@Dao
interface WordDao{

    @Query("SELECT * FROM WORD_TABLE ORDER BY word ASC")
    fun getAlphabetizedWords(): Flow<List<Word>>// Note: Estamos usando flow (o video de Paging Lib 3 explica)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(word: Word)

    @Query("DELETE FROM word_table")//que magica e essa? Lembrando não faça delete sem where no seu codigo
    fun deleteAll()
}

// +-----------------------------------------------------------------------------------------------+
// | >>> 3° <<< REPOSITORY: MEDIADOR DE ACESSO A DADOS LOCAIS OU REMOTO CASO EXISTAM VÁRIAS FONTES |
// +-----------------------------------------------------------------------------------------------+
// Declare o DAO como uma propriedade privada no construtor. Passe apenas o DAO
// em vez do banco de dados inteiro, porque você só precisamos acessar o DAO

class WordRepository(private val wordDao: WordDao): Repository{

    // Room executa todas as consultas em um thread separado.
    // Flow observado notificará o observador quando os dados forem alterados.
    // val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()

    // Por padrão, o Room executa consultas suspensas fora do thread principal, portanto,
    // não precisamos implementar qualquer outra coisa para garantir que não estamos fazendo
    // um trabalho de banco de dados de longa duração fora da Thread principal.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun insert(word: Word) {
       wordDao.insert(word)
    }

    override fun observeWords(): Flow<List<Word>> {
       return wordDao.getAlphabetizedWords()
    }
}
// +-----------------------------------------------------------------------+
// | >>> TESTANDO REPOSITORY <<< INTERFACE TORNA REPOSITORIO INTERCAMBIÁVEL|
// +-----------------------------------------------------------------------+
class MockWordRepository(initialTestWords: List<Word>): Repository {

    private val allWords = mutableListOf<Word>()
    private var wordsFlow = flowOf<List<Word>>()

    init {
        allWords.addAll(initialTestWords)
        wordsFlow = flowOf<List<Word>>(allWords)
    }
    override suspend fun insert(word: Word) {
        allWords.add(word)
        wordsFlow = flowOf<List<Word>>(allWords)
    }

    override fun observeWords(): Flow<List<Word>> = wordsFlow

}
// +-----------------------------------------------------------------------+
// | >>> TESTANDO REPOSITORY <<< INTERFACE TORNA REPOSITORIO INTERCAMBIÁVEL|
// +-----------------------------------------------------------------------+
interface Repository {
    suspend fun insert(word: Word)
    fun observeWords(): Flow<List<Word>>
}

// +-----------------------------------------------------------------------+
// | >>> 4° <<< ROOM - NOSSO BANCO DE DADOS COM AS DEFINICÕES DE TABELAS   |
// +-----------------------------------------------------------------------+
@Database(entities = arrayOf(Word::class), version = 1, exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase(){
    abstract fun wordDao(): WordDao

    // +----------------------------------------------------------------------------------+
    // | CALLBACK - SERA USADA PARA INICIALIZAR O NOSSO BANCO DE DADOS NA HORA DA CRIACÃO |
    // +----------------------------------------------------------------------------------+

    private class WordDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback(){

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.wordDao())// So pra exemplificar como pre-popular um banco ce dados
                }
            }
        }

        suspend fun  populateDatabase(wordDao: WordDao){
            // APAGAMOS TUDO QUE ESTEJA NO BANCO PRIMEIRO (NÃO FACA ISSO EM PRODUCÃO)
            wordDao.deleteAll()

            // ADICIONAMOS ALGUNS VALORES ALEATÓRIOS PARA TER ALGO PARA EXIBIR
            wordDao.insert(Word("Olá, treine bastante "))
            wordDao.insert(Word("VC, é o melhor!!!"))

        }
    }

    companion object{
        // +------------------------------------------------------------------------------------+
        // | SINGLETON - PREVINE QUE MULTIPLAS INSTANCIAS DO BANCO SEJAM ABERTAS AO MESMO TEMPO |
        // |             PADRÃO DE PROJETO QUE SO DEIXA UMA INSTANCIA DO MESMO OBJETO EXISTIR   |
        // +------------------------------------------------------------------------------------+
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): WordRoomDatabase {
            // Se o banco de dados já existir, retorne direto, do contrario crie o banco
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_database" //nome do banco de dados
                ).addCallback(WordDatabaseCallback(scope)).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// +---------------------------------------------------------------------------+
// | >>> 5° <<< VIEW MODEL: ATUALIZAR DADOS E RETEM A LÓGICA DA UI EM QUESTÃO  |
// +---------------------------------------------------------------------------+
class WordViewModel(private val repository: Repository) : ViewModel(){
    // Usar LiveData e armazenar em cache o que allWords retorna tem vários benefícios:
    // - Podemos colocar um observador nos dados (em vez de pesquisar as alterações) e apenas
    //   atualizar a IU quando os dados realmente mudam.
    // - O repositório é completamente separado da IU por meio do ViewModel.
    val allWords: LiveData<List<Word>> = repository.observeWords().asLiveData()

    /** Lançamento de uma nova co-rotina para inserir os dados de forma não bloqueadora */
    fun insert (word: Word) = viewModelScope.launch {
        repository.insert(word)
    }
}

// +---------------------------------------------------------+
// | >>> 6° <<< MODEL FACTORY: CRIADOR DE OBJETOS COMPLEXOS  |
// +---------------------------------------------------------+
class WordViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java))
            return WordViewModel(repository) as T
        throw IllegalArgumentException("MODELO DESCONHECIDO ")

    }
}