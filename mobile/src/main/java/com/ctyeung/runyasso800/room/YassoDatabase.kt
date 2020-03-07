package com.ctyeung.runyasso800.room

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.splits.SplitDao
import com.ctyeung.runyasso800.room.steps.Step
import com.ctyeung.runyasso800.room.steps.StepDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/*
 * References
 * https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#2
 */
@Database(  entities = [Split::class, Step::class],
            version = 1,
            exportSchema = false)
public abstract class YassoDatabase : RoomDatabase ()
{
    abstract fun splitDao():SplitDao
    abstract fun stepDao():StepDao

    companion object {

        @JvmField
        val MIGRATION_1_2 = Migration1To2()

        @Volatile
        private var INSTANCE: YassoDatabase? = null

        fun getDatabase(context:Context,
                        scope:CoroutineScope):YassoDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    YassoDatabase::class.java,
                    "yasso_database"
                ).addCallback(SplitDatabaseCallback(scope))
                    //       .addMigrations(VerseDatabase.MIGRATION_1_2)
                    .build()

                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    private class SplitDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    /*
                     * Refactor !!! Use factory pattern here !
                     */
                    populateDatabase(database.splitDao(), database.stepDao())
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        suspend fun populateDatabase(splitDao: SplitDao,
                                     stepDao:StepDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            splitDao.deleteAll()

            var split = Split(0,
                    Split.RUN_TYPE_SPRINT,
                    800.0,
                    java.lang.System.currentTimeMillis(),
                    0.0,
                    0.0,
                    java.lang.System.currentTimeMillis()+ 100,
                0.0,
                0.0)
            splitDao.insert(split)

            stepDao.deleteAll()
            var step = Step(0,
                            0,
                            0.0,
                            Split.RUN_TYPE_SPRINT,
                            java.lang.System.currentTimeMillis(),
                            0.0,
                            0.0)

            stepDao.insert(step)

            /*
             * actually need to delete above for a fresh start
             */
        }
    }

    /*
     * References - migration
     * https://medium.com/@manuelvicnt/android-room-upgrading-alpha-versions-needs-a-migration-with-kotlin-or-nonnull-7a2d140f05b9
     */
    class Migration1To2 : Migration(1,2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            val TABLE_NAME_TEMP = "GameNew"

            // 1. Create new table
            database.execSQL("CREATE TABLE IF NOT EXISTS `$TABLE_NAME_TEMP` " +
                    "(PRIMARY KEY(`datetime` Long) + (`verse` TEXT NOT NULL)")

            // 2. Copy the data
/*            database.execSQL("INSERT INTO $TABLE_NAME_TEMP (game_name) "
                    + "SELECT game_name "
                    + "FROM $TABLE_NAME")
*/
            // 3. Remove the old table
            database.execSQL("DROP TABLE split_table")

            // 4. Change the table name to the correct one
            database.execSQL("ALTER TABLE $TABLE_NAME_TEMP RENAME TO split_table")
        }
    }
}