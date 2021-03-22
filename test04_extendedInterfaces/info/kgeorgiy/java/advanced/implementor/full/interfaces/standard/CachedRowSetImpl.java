package info.kgeorgiy.java.advanced.implementor.full.interfaces.standard;
public class CachedRowSetImpl implements info.kgeorgiy.java.advanced.implementor.full.interfaces.standard.CachedRowSet {

    public int size(){
        return 0;
    }
    public void execute(java.sql.Connection arg1){
        return;
    }
    public void release(){
        return;
    }
    public java.util.Collection toCollection(){
        return null;
    }
    public java.util.Collection toCollection(java.lang.String arg1){
        return null;
    }
    public java.util.Collection toCollection(int arg1){
        return null;
    }
    public void commit(){
        return;
    }
    public void rollback(){
        return;
    }
    public void rollback(java.sql.Savepoint arg1){
        return;
    }
    public void populate(java.sql.ResultSet arg1, int arg2){
        return;
    }
    public void populate(java.sql.ResultSet arg1){
        return;
    }
    public void acceptChanges(java.sql.Connection arg1){
        return;
    }
    public void acceptChanges(){
        return;
    }
    public void restoreOriginal(){
        return;
    }
    public void undoDelete(){
        return;
    }
    public void undoInsert(){
        return;
    }
    public void undoUpdate(){
        return;
    }
    public boolean columnUpdated(int arg1){
        return false;
    }
    public boolean columnUpdated(java.lang.String arg1){
        return false;
    }
    public javax.sql.rowset.spi.SyncProvider getSyncProvider(){
        return null;
    }
    public void setSyncProvider(java.lang.String arg1){
        return;
    }
    public void setMetaData(javax.sql.RowSetMetaData arg1){
        return;
    }
    public java.sql.ResultSet getOriginal(){
        return null;
    }
    public java.sql.ResultSet getOriginalRow(){
        return null;
    }
    public void setOriginalRow(){
        return;
    }
    public java.lang.String getTableName(){
        return null;
    }
    public void setTableName(java.lang.String arg1){
        return;
    }
    public int[] getKeyColumns(){
        return null;
    }
    public void setKeyColumns(int[] arg1){
        return;
    }
    public javax.sql.RowSet createShared(){
        return null;
    }
    public javax.sql.rowset.CachedRowSet createCopy(){
        return null;
    }
    public javax.sql.rowset.CachedRowSet createCopySchema(){
        return null;
    }
    public javax.sql.rowset.CachedRowSet createCopyNoConstraints(){
        return null;
    }
    public javax.sql.rowset.RowSetWarning getRowSetWarnings(){
        return null;
    }
    public boolean getShowDeleted(){
        return false;
    }
    public void setShowDeleted(boolean arg1){
        return;
    }
    public void rowSetPopulated(javax.sql.RowSetEvent arg1, int arg2){
        return;
    }
    public void setPageSize(int arg1){
        return;
    }
    public int getPageSize(){
        return 0;
    }
    public boolean nextPage(){
        return false;
    }
    public boolean previousPage(){
        return false;
    }
    public void execute(){
        return;
    }
    public void setReadOnly(boolean arg1){
        return;
    }
    public void setBoolean(int arg1, boolean arg2){
        return;
    }
    public void setBoolean(java.lang.String arg1, boolean arg2){
        return;
    }
    public void setByte(int arg1, byte arg2){
        return;
    }
    public void setByte(java.lang.String arg1, byte arg2){
        return;
    }
    public void setShort(java.lang.String arg1, short arg2){
        return;
    }
    public void setShort(int arg1, short arg2){
        return;
    }
    public void setInt(java.lang.String arg1, int arg2){
        return;
    }
    public void setInt(int arg1, int arg2){
        return;
    }
    public void setLong(int arg1, long arg2){
        return;
    }
    public void setLong(java.lang.String arg1, long arg2){
        return;
    }
    public void setFloat(java.lang.String arg1, float arg2){
        return;
    }
    public void setFloat(int arg1, float arg2){
        return;
    }
    public void setDouble(java.lang.String arg1, double arg2){
        return;
    }
    public void setDouble(int arg1, double arg2){
        return;
    }
    public boolean isReadOnly(){
        return false;
    }
    public void setURL(int arg1, java.net.URL arg2){
        return;
    }
    public void setArray(int arg1, java.sql.Array arg2){
        return;
    }
    public void setTime(java.lang.String arg1, java.sql.Time arg2, java.util.Calendar arg3){
        return;
    }
    public void setTime(int arg1, java.sql.Time arg2, java.util.Calendar arg3){
        return;
    }
    public void setTime(java.lang.String arg1, java.sql.Time arg2){
        return;
    }
    public void setTime(int arg1, java.sql.Time arg2){
        return;
    }
    public void setDate(java.lang.String arg1, java.sql.Date arg2){
        return;
    }
    public void setDate(int arg1, java.sql.Date arg2, java.util.Calendar arg3){
        return;
    }
    public void setDate(int arg1, java.sql.Date arg2){
        return;
    }
    public void setDate(java.lang.String arg1, java.sql.Date arg2, java.util.Calendar arg3){
        return;
    }
    public java.lang.String getUrl(){
        return null;
    }
    public void setUrl(java.lang.String arg1){
        return;
    }
    public java.lang.String getDataSourceName(){
        return null;
    }
    public void setDataSourceName(java.lang.String arg1){
        return;
    }
    public java.lang.String getUsername(){
        return null;
    }
    public void setUsername(java.lang.String arg1){
        return;
    }
    public void setPassword(java.lang.String arg1){
        return;
    }
    public int getTransactionIsolation(){
        return 0;
    }
    public void setTransactionIsolation(int arg1){
        return;
    }
    public java.util.Map getTypeMap(){
        return null;
    }
    public void setTypeMap(java.util.Map arg1){
        return;
    }
    public java.lang.String getCommand(){
        return null;
    }
    public void setCommand(java.lang.String arg1){
        return;
    }
    public int getMaxFieldSize(){
        return 0;
    }
    public void setMaxFieldSize(int arg1){
        return;
    }
    public int getMaxRows(){
        return 0;
    }
    public void setMaxRows(int arg1){
        return;
    }
    public boolean getEscapeProcessing(){
        return false;
    }
    public void setEscapeProcessing(boolean arg1){
        return;
    }
    public int getQueryTimeout(){
        return 0;
    }
    public void setQueryTimeout(int arg1){
        return;
    }
    public void setConcurrency(int arg1){
        return;
    }
    public void setNull(java.lang.String arg1, int arg2){
        return;
    }
    public void setNull(java.lang.String arg1, int arg2, java.lang.String arg3){
        return;
    }
    public void setNull(int arg1, int arg2){
        return;
    }
    public void setNull(int arg1, int arg2, java.lang.String arg3){
        return;
    }
    public void setBigDecimal(java.lang.String arg1, java.math.BigDecimal arg2){
        return;
    }
    public void setBigDecimal(int arg1, java.math.BigDecimal arg2){
        return;
    }
    public void setString(int arg1, java.lang.String arg2){
        return;
    }
    public void setString(java.lang.String arg1, java.lang.String arg2){
        return;
    }
    public void setBytes(int arg1, byte[] arg2){
        return;
    }
    public void setBytes(java.lang.String arg1, byte[] arg2){
        return;
    }
    public void setTimestamp(java.lang.String arg1, java.sql.Timestamp arg2){
        return;
    }
    public void setTimestamp(int arg1, java.sql.Timestamp arg2){
        return;
    }
    public void setTimestamp(java.lang.String arg1, java.sql.Timestamp arg2, java.util.Calendar arg3){
        return;
    }
    public void setTimestamp(int arg1, java.sql.Timestamp arg2, java.util.Calendar arg3){
        return;
    }
    public void setAsciiStream(java.lang.String arg1, java.io.InputStream arg2, int arg3){
        return;
    }
    public void setAsciiStream(int arg1, java.io.InputStream arg2){
        return;
    }
    public void setAsciiStream(java.lang.String arg1, java.io.InputStream arg2){
        return;
    }
    public void setAsciiStream(int arg1, java.io.InputStream arg2, int arg3){
        return;
    }
    public void setBinaryStream(java.lang.String arg1, java.io.InputStream arg2, int arg3){
        return;
    }
    public void setBinaryStream(int arg1, java.io.InputStream arg2, int arg3){
        return;
    }
    public void setBinaryStream(java.lang.String arg1, java.io.InputStream arg2){
        return;
    }
    public void setBinaryStream(int arg1, java.io.InputStream arg2){
        return;
    }
    public void setCharacterStream(int arg1, java.io.Reader arg2, int arg3){
        return;
    }
    public void setCharacterStream(java.lang.String arg1, java.io.Reader arg2, int arg3){
        return;
    }
    public void setCharacterStream(java.lang.String arg1, java.io.Reader arg2){
        return;
    }
    public void setCharacterStream(int arg1, java.io.Reader arg2){
        return;
    }
    public void setNCharacterStream(int arg1, java.io.Reader arg2){
        return;
    }
    public void setNCharacterStream(java.lang.String arg1, java.io.Reader arg2){
        return;
    }
    public void setNCharacterStream(java.lang.String arg1, java.io.Reader arg2, long arg3){
        return;
    }
    public void setNCharacterStream(int arg1, java.io.Reader arg2, long arg3){
        return;
    }
    public void setObject(java.lang.String arg1, java.lang.Object arg2){
        return;
    }
    public void setObject(java.lang.String arg1, java.lang.Object arg2, int arg3, int arg4){
        return;
    }
    public void setObject(int arg1, java.lang.Object arg2){
        return;
    }
    public void setObject(int arg1, java.lang.Object arg2, int arg3, int arg4){
        return;
    }
    public void setObject(java.lang.String arg1, java.lang.Object arg2, int arg3){
        return;
    }
    public void setObject(int arg1, java.lang.Object arg2, int arg3){
        return;
    }
    public void setRef(int arg1, java.sql.Ref arg2){
        return;
    }
    public void setBlob(java.lang.String arg1, java.io.InputStream arg2, long arg3){
        return;
    }
    public void setBlob(java.lang.String arg1, java.sql.Blob arg2){
        return;
    }
    public void setBlob(int arg1, java.io.InputStream arg2){
        return;
    }
    public void setBlob(int arg1, java.io.InputStream arg2, long arg3){
        return;
    }
    public void setBlob(int arg1, java.sql.Blob arg2){
        return;
    }
    public void setBlob(java.lang.String arg1, java.io.InputStream arg2){
        return;
    }
    public void setClob(int arg1, java.io.Reader arg2, long arg3){
        return;
    }
    public void setClob(java.lang.String arg1, java.io.Reader arg2, long arg3){
        return;
    }
    public void setClob(java.lang.String arg1, java.io.Reader arg2){
        return;
    }
    public void setClob(int arg1, java.io.Reader arg2){
        return;
    }
    public void setClob(java.lang.String arg1, java.sql.Clob arg2){
        return;
    }
    public void setClob(int arg1, java.sql.Clob arg2){
        return;
    }
    public void clearParameters(){
        return;
    }
    public void addRowSetListener(javax.sql.RowSetListener arg1){
        return;
    }
    public void removeRowSetListener(javax.sql.RowSetListener arg1){
        return;
    }
    public void setSQLXML(int arg1, java.sql.SQLXML arg2){
        return;
    }
    public void setSQLXML(java.lang.String arg1, java.sql.SQLXML arg2){
        return;
    }
    public void setRowId(int arg1, java.sql.RowId arg2){
        return;
    }
    public void setRowId(java.lang.String arg1, java.sql.RowId arg2){
        return;
    }
    public void setNString(java.lang.String arg1, java.lang.String arg2){
        return;
    }
    public void setNString(int arg1, java.lang.String arg2){
        return;
    }
    public void setNClob(int arg1, java.io.Reader arg2, long arg3){
        return;
    }
    public void setNClob(java.lang.String arg1, java.io.Reader arg2){
        return;
    }
    public void setNClob(int arg1, java.sql.NClob arg2){
        return;
    }
    public void setNClob(int arg1, java.io.Reader arg2){
        return;
    }
    public void setNClob(java.lang.String arg1, java.io.Reader arg2, long arg3){
        return;
    }
    public void setNClob(java.lang.String arg1, java.sql.NClob arg2){
        return;
    }
    public java.lang.String getPassword(){
        return null;
    }
    public void setType(int arg1){
        return;
    }
    public void updateBytes(int arg1, byte[] arg2){
        return;
    }
    public void updateBytes(java.lang.String arg1, byte[] arg2){
        return;
    }
    public boolean getBoolean(java.lang.String arg1){
        return false;
    }
    public boolean getBoolean(int arg1){
        return false;
    }
    public byte getByte(java.lang.String arg1){
        return 0;
    }
    public byte getByte(int arg1){
        return 0;
    }
    public short getShort(java.lang.String arg1){
        return 0;
    }
    public short getShort(int arg1){
        return 0;
    }
    public int getInt(int arg1){
        return 0;
    }
    public int getInt(java.lang.String arg1){
        return 0;
    }
    public long getLong(java.lang.String arg1){
        return 0;
    }
    public long getLong(int arg1){
        return 0;
    }
    public float getFloat(java.lang.String arg1){
        return 0.0f;
    }
    public float getFloat(int arg1){
        return 0.0f;
    }
    public double getDouble(int arg1){
        return 0.0;
    }
    public double getDouble(java.lang.String arg1){
        return 0.0;
    }
    public byte[] getBytes(java.lang.String arg1){
        return null;
    }
    public byte[] getBytes(int arg1){
        return null;
    }
    public boolean next(){
        return false;
    }
    public boolean last(){
        return false;
    }
    public boolean first(){
        return false;
    }
    public void close(){
        return;
    }
    public int getType(){
        return 0;
    }
    public java.lang.Object getObject(int arg1){
        return null;
    }
    public java.lang.Object getObject(java.lang.String arg1){
        return null;
    }
    public java.lang.Object getObject(java.lang.String arg1, java.lang.Class arg2){
        return null;
    }
    public java.lang.Object getObject(int arg1, java.util.Map arg2){
        return null;
    }
    public java.lang.Object getObject(java.lang.String arg1, java.util.Map arg2){
        return null;
    }
    public java.lang.Object getObject(int arg1, java.lang.Class arg2){
        return null;
    }
    public java.sql.Ref getRef(int arg1){
        return null;
    }
    public java.sql.Ref getRef(java.lang.String arg1){
        return null;
    }
    public boolean previous(){
        return false;
    }
    public boolean absolute(int arg1){
        return false;
    }
    public java.sql.Array getArray(java.lang.String arg1){
        return null;
    }
    public java.sql.Array getArray(int arg1){
        return null;
    }
    public java.sql.Time getTime(int arg1){
        return null;
    }
    public java.sql.Time getTime(java.lang.String arg1){
        return null;
    }
    public java.sql.Time getTime(java.lang.String arg1, java.util.Calendar arg2){
        return null;
    }
    public java.sql.Time getTime(int arg1, java.util.Calendar arg2){
        return null;
    }
    public java.sql.Timestamp getTimestamp(java.lang.String arg1, java.util.Calendar arg2){
        return null;
    }
    public java.sql.Timestamp getTimestamp(int arg1, java.util.Calendar arg2){
        return null;
    }
    public java.sql.Timestamp getTimestamp(int arg1){
        return null;
    }
    public java.sql.Timestamp getTimestamp(java.lang.String arg1){
        return null;
    }
    public java.lang.String getString(java.lang.String arg1){
        return null;
    }
    public java.lang.String getString(int arg1){
        return null;
    }
    public java.net.URL getURL(int arg1){
        return null;
    }
    public java.net.URL getURL(java.lang.String arg1){
        return null;
    }
    public void updateTime(int arg1, java.sql.Time arg2){
        return;
    }
    public void updateTime(java.lang.String arg1, java.sql.Time arg2){
        return;
    }
    public boolean relative(int arg1){
        return false;
    }
    public java.math.BigDecimal getBigDecimal(java.lang.String arg1){
        return null;
    }
    public java.math.BigDecimal getBigDecimal(int arg1){
        return null;
    }
    public java.math.BigDecimal getBigDecimal(int arg1, int arg2){
        return null;
    }
    public java.math.BigDecimal getBigDecimal(java.lang.String arg1, int arg2){
        return null;
    }
    public java.sql.Date getDate(java.lang.String arg1, java.util.Calendar arg2){
        return null;
    }
    public java.sql.Date getDate(java.lang.String arg1){
        return null;
    }
    public java.sql.Date getDate(int arg1, java.util.Calendar arg2){
        return null;
    }
    public java.sql.Date getDate(int arg1){
        return null;
    }
    public java.sql.Statement getStatement(){
        return null;
    }
    public boolean isFirst(){
        return false;
    }
    public boolean wasNull(){
        return false;
    }
    public java.io.InputStream getAsciiStream(java.lang.String arg1){
        return null;
    }
    public java.io.InputStream getAsciiStream(int arg1){
        return null;
    }
    public java.io.InputStream getUnicodeStream(java.lang.String arg1){
        return null;
    }
    public java.io.InputStream getUnicodeStream(int arg1){
        return null;
    }
    public java.io.InputStream getBinaryStream(java.lang.String arg1){
        return null;
    }
    public java.io.InputStream getBinaryStream(int arg1){
        return null;
    }
    public java.sql.SQLWarning getWarnings(){
        return null;
    }
    public void clearWarnings(){
        return;
    }
    public java.lang.String getCursorName(){
        return null;
    }
    public java.sql.ResultSetMetaData getMetaData(){
        return null;
    }
    public int findColumn(java.lang.String arg1){
        return 0;
    }
    public java.io.Reader getCharacterStream(java.lang.String arg1){
        return null;
    }
    public java.io.Reader getCharacterStream(int arg1){
        return null;
    }
    public boolean isBeforeFirst(){
        return false;
    }
    public boolean isAfterLast(){
        return false;
    }
    public boolean isLast(){
        return false;
    }
    public void beforeFirst(){
        return;
    }
    public void afterLast(){
        return;
    }
    public int getRow(){
        return 0;
    }
    public void setFetchDirection(int arg1){
        return;
    }
    public int getFetchDirection(){
        return 0;
    }
    public void setFetchSize(int arg1){
        return;
    }
    public int getFetchSize(){
        return 0;
    }
    public int getConcurrency(){
        return 0;
    }
    public boolean rowUpdated(){
        return false;
    }
    public boolean rowInserted(){
        return false;
    }
    public boolean rowDeleted(){
        return false;
    }
    public void updateNull(int arg1){
        return;
    }
    public void updateNull(java.lang.String arg1){
        return;
    }
    public void updateBoolean(java.lang.String arg1, boolean arg2){
        return;
    }
    public void updateBoolean(int arg1, boolean arg2){
        return;
    }
    public void updateByte(int arg1, byte arg2){
        return;
    }
    public void updateByte(java.lang.String arg1, byte arg2){
        return;
    }
    public void updateShort(int arg1, short arg2){
        return;
    }
    public void updateShort(java.lang.String arg1, short arg2){
        return;
    }
    public void updateInt(java.lang.String arg1, int arg2){
        return;
    }
    public void updateInt(int arg1, int arg2){
        return;
    }
    public void updateLong(java.lang.String arg1, long arg2){
        return;
    }
    public void updateLong(int arg1, long arg2){
        return;
    }
    public void updateFloat(int arg1, float arg2){
        return;
    }
    public void updateFloat(java.lang.String arg1, float arg2){
        return;
    }
    public void updateDouble(int arg1, double arg2){
        return;
    }
    public void updateDouble(java.lang.String arg1, double arg2){
        return;
    }
    public void updateBigDecimal(int arg1, java.math.BigDecimal arg2){
        return;
    }
    public void updateBigDecimal(java.lang.String arg1, java.math.BigDecimal arg2){
        return;
    }
    public void updateString(java.lang.String arg1, java.lang.String arg2){
        return;
    }
    public void updateString(int arg1, java.lang.String arg2){
        return;
    }
    public void updateDate(java.lang.String arg1, java.sql.Date arg2){
        return;
    }
    public void updateDate(int arg1, java.sql.Date arg2){
        return;
    }
    public void updateTimestamp(java.lang.String arg1, java.sql.Timestamp arg2){
        return;
    }
    public void updateTimestamp(int arg1, java.sql.Timestamp arg2){
        return;
    }
    public void updateAsciiStream(java.lang.String arg1, java.io.InputStream arg2, int arg3){
        return;
    }
    public void updateAsciiStream(int arg1, java.io.InputStream arg2, long arg3){
        return;
    }
    public void updateAsciiStream(java.lang.String arg1, java.io.InputStream arg2, long arg3){
        return;
    }
    public void updateAsciiStream(int arg1, java.io.InputStream arg2, int arg3){
        return;
    }
    public void updateAsciiStream(java.lang.String arg1, java.io.InputStream arg2){
        return;
    }
    public void updateAsciiStream(int arg1, java.io.InputStream arg2){
        return;
    }
    public void updateBinaryStream(java.lang.String arg1, java.io.InputStream arg2, long arg3){
        return;
    }
    public void updateBinaryStream(int arg1, java.io.InputStream arg2){
        return;
    }
    public void updateBinaryStream(int arg1, java.io.InputStream arg2, int arg3){
        return;
    }
    public void updateBinaryStream(int arg1, java.io.InputStream arg2, long arg3){
        return;
    }
    public void updateBinaryStream(java.lang.String arg1, java.io.InputStream arg2){
        return;
    }
    public void updateBinaryStream(java.lang.String arg1, java.io.InputStream arg2, int arg3){
        return;
    }
    public void updateCharacterStream(int arg1, java.io.Reader arg2, int arg3){
        return;
    }
    public void updateCharacterStream(int arg1, java.io.Reader arg2){
        return;
    }
    public void updateCharacterStream(java.lang.String arg1, java.io.Reader arg2, int arg3){
        return;
    }
    public void updateCharacterStream(java.lang.String arg1, java.io.Reader arg2, long arg3){
        return;
    }
    public void updateCharacterStream(int arg1, java.io.Reader arg2, long arg3){
        return;
    }
    public void updateCharacterStream(java.lang.String arg1, java.io.Reader arg2){
        return;
    }
    public void updateObject(int arg1, java.lang.Object arg2, java.sql.SQLType arg3){
        return;
    }
    public void updateObject(int arg1, java.lang.Object arg2, java.sql.SQLType arg3, int arg4){
        return;
    }
    public void updateObject(java.lang.String arg1, java.lang.Object arg2, java.sql.SQLType arg3, int arg4){
        return;
    }
    public void updateObject(java.lang.String arg1, java.lang.Object arg2, java.sql.SQLType arg3){
        return;
    }
    public void updateObject(java.lang.String arg1, java.lang.Object arg2, int arg3){
        return;
    }
    public void updateObject(int arg1, java.lang.Object arg2){
        return;
    }
    public void updateObject(int arg1, java.lang.Object arg2, int arg3){
        return;
    }
    public void updateObject(java.lang.String arg1, java.lang.Object arg2){
        return;
    }
    public void insertRow(){
        return;
    }
    public void updateRow(){
        return;
    }
    public void deleteRow(){
        return;
    }
    public void refreshRow(){
        return;
    }
    public void cancelRowUpdates(){
        return;
    }
    public void moveToInsertRow(){
        return;
    }
    public void moveToCurrentRow(){
        return;
    }
    public java.sql.Blob getBlob(java.lang.String arg1){
        return null;
    }
    public java.sql.Blob getBlob(int arg1){
        return null;
    }
    public java.sql.Clob getClob(int arg1){
        return null;
    }
    public java.sql.Clob getClob(java.lang.String arg1){
        return null;
    }
    public void updateRef(int arg1, java.sql.Ref arg2){
        return;
    }
    public void updateRef(java.lang.String arg1, java.sql.Ref arg2){
        return;
    }
    public void updateBlob(int arg1, java.sql.Blob arg2){
        return;
    }
    public void updateBlob(java.lang.String arg1, java.io.InputStream arg2){
        return;
    }
    public void updateBlob(int arg1, java.io.InputStream arg2){
        return;
    }
    public void updateBlob(int arg1, java.io.InputStream arg2, long arg3){
        return;
    }
    public void updateBlob(java.lang.String arg1, java.io.InputStream arg2, long arg3){
        return;
    }
    public void updateBlob(java.lang.String arg1, java.sql.Blob arg2){
        return;
    }
    public void updateClob(int arg1, java.sql.Clob arg2){
        return;
    }
    public void updateClob(java.lang.String arg1, java.io.Reader arg2, long arg3){
        return;
    }
    public void updateClob(java.lang.String arg1, java.sql.Clob arg2){
        return;
    }
    public void updateClob(int arg1, java.io.Reader arg2, long arg3){
        return;
    }
    public void updateClob(java.lang.String arg1, java.io.Reader arg2){
        return;
    }
    public void updateClob(int arg1, java.io.Reader arg2){
        return;
    }
    public void updateArray(int arg1, java.sql.Array arg2){
        return;
    }
    public void updateArray(java.lang.String arg1, java.sql.Array arg2){
        return;
    }
    public java.sql.RowId getRowId(int arg1){
        return null;
    }
    public java.sql.RowId getRowId(java.lang.String arg1){
        return null;
    }
    public void updateRowId(java.lang.String arg1, java.sql.RowId arg2){
        return;
    }
    public void updateRowId(int arg1, java.sql.RowId arg2){
        return;
    }
    public int getHoldability(){
        return 0;
    }
    public void updateNString(int arg1, java.lang.String arg2){
        return;
    }
    public void updateNString(java.lang.String arg1, java.lang.String arg2){
        return;
    }
    public void updateNClob(int arg1, java.io.Reader arg2){
        return;
    }
    public void updateNClob(java.lang.String arg1, java.io.Reader arg2){
        return;
    }
    public void updateNClob(int arg1, java.io.Reader arg2, long arg3){
        return;
    }
    public void updateNClob(java.lang.String arg1, java.io.Reader arg2, long arg3){
        return;
    }
    public void updateNClob(int arg1, java.sql.NClob arg2){
        return;
    }
    public void updateNClob(java.lang.String arg1, java.sql.NClob arg2){
        return;
    }
    public java.sql.NClob getNClob(int arg1){
        return null;
    }
    public java.sql.NClob getNClob(java.lang.String arg1){
        return null;
    }
    public java.sql.SQLXML getSQLXML(java.lang.String arg1){
        return null;
    }
    public java.sql.SQLXML getSQLXML(int arg1){
        return null;
    }
    public void updateSQLXML(java.lang.String arg1, java.sql.SQLXML arg2){
        return;
    }
    public void updateSQLXML(int arg1, java.sql.SQLXML arg2){
        return;
    }
    public java.lang.String getNString(int arg1){
        return null;
    }
    public java.lang.String getNString(java.lang.String arg1){
        return null;
    }
    public java.io.Reader getNCharacterStream(int arg1){
        return null;
    }
    public java.io.Reader getNCharacterStream(java.lang.String arg1){
        return null;
    }
    public void updateNCharacterStream(int arg1, java.io.Reader arg2){
        return;
    }
    public void updateNCharacterStream(java.lang.String arg1, java.io.Reader arg2){
        return;
    }
    public void updateNCharacterStream(java.lang.String arg1, java.io.Reader arg2, long arg3){
        return;
    }
    public void updateNCharacterStream(int arg1, java.io.Reader arg2, long arg3){
        return;
    }
    public boolean isClosed(){
        return false;
    }
    public java.lang.Object unwrap(java.lang.Class arg1){
        return null;
    }
    public boolean isWrapperFor(java.lang.Class arg1){
        return false;
    }
    public void setMatchColumn(java.lang.String[] arg1){
        return;
    }
    public void setMatchColumn(java.lang.String arg1){
        return;
    }
    public void setMatchColumn(int[] arg1){
        return;
    }
    public void setMatchColumn(int arg1){
        return;
    }
    public int[] getMatchColumnIndexes(){
        return null;
    }
    public java.lang.String[] getMatchColumnNames(){
        return null;
    }
    public void unsetMatchColumn(java.lang.String[] arg1){
        return;
    }
    public void unsetMatchColumn(java.lang.String arg1){
        return;
    }
    public void unsetMatchColumn(int[] arg1){
        return;
    }
    public void unsetMatchColumn(int arg1){
        return;
    }
}