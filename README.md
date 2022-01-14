# HSQLDB 2.16.0 CLOB corruption bug demo

See the reported bug 
[**CLOB value saved truncated when encryption is used**](https://sourceforge.net/p/hsqldb/bugs/1645/)

After upgrading to 2.16 we faced the issue that `CLOB` fields are saved corrupted (truncated) when flushed/persisted to the file.

**Environment:**
* Windows, Linux
* JDBC driver hsqldb-2.6.0 and 2.6.1
* Hibernate 5.5.8.Final
* Spring Boot 2.5.6

**Steps to reproduce:**
1. Configure JDBC connection string with blowfish encryption and lob crypt:
   `jdbc:hsqldb:file:XXXXXXX;crypt_key=11111111111111111111111111111111;crypt_type=blowfish;crypt_lobs=true`
2. Configure @Entity with @Lob String filed and explicit size more than 512 KiB (otherwise default 255 symbols length would be applied):
    ```java
    @Lob
    @Column(length = 1024 * 1024 * 1024) // 1GiB
    private String corruptedValue;
    ```

3. Create Spring CRUD repository for this entity.
4. Save value with large string (about 1 MiB) to the repository
5. Force writing to disk (you better know how to do this, i waited 1 second, also stop application persists to disk)
6. Read persisted value and compare saved String with the original one

**Actual Result:**
String is truncated to 512 KiB

**Expected Result:**
String is not changed

**Note**:
* `BLOB` type still works fine (byte[] java type)
* Without encryption - works fine
* Less than 512 KiB - works fine
* Version 2.15.0 - works fine

Pls follow the [`PersistenceConfiguration`](src/main/java/org/hsqldb/corrupted/PersistenceConfiguration.java).

Corrupted entity configuration: [`ClobCorrupted`](src/main/java/org/hsqldb/corrupted/model/ClobCorrupted.java).

Test the behavior in [`CorruptedDataSavedExample`](src/main/java/org/hsqldb/corrupted/CorruptedDataSavedExample.java) 
(Spring Boot Application) and provided test classes
(`ClobCorruptedTest`)(src/test/java/org/hsqldb/corrupted/ClobCorruptionTest.java)