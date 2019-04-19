package com.brt.gpp.comum.conexoes.bancoDados;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

/**
 * A classe GPPResultSet eh responsavel por criar a associacao entre
 * um Statement e um ResultSet gerado por ele, de forma implementar
 * um <I>override</I> do metodo close(), tal que o Statement seja
 * fechado junto com o ResultSet e assim, evitar o erro "Maximum Open
 * Cursos Exceeded".  
 * @author Gustavo Gusmao
 *
 */
public class GPPResultSet implements ResultSet
{
    private ResultSet rs;
    private Statement stmt;
    Collection listStatement;
    
    public GPPResultSet()
    {
    }
    
    public GPPResultSet(ResultSet rs, Statement stmt, Collection listStatement)
    {
        this.rs = rs;
        this.stmt = stmt;
        this.listStatement = listStatement;
    }
    

    /**
     * @param row
     * @return
     * @throws java.sql.SQLException
     */
    public boolean absolute(int row) throws SQLException
    {
        return rs.absolute(row);
    }
    /**
     * @throws java.sql.SQLException
     */
    public void afterLast() throws SQLException
    {
        rs.afterLast();
    }
    /**
     * @throws java.sql.SQLException
     */
    public void beforeFirst() throws SQLException
    {
        rs.beforeFirst();
    }
    /**
     * @throws java.sql.SQLException
     */
    public void cancelRowUpdates() throws SQLException
    {
        rs.cancelRowUpdates();
    }
    /**
     * @throws java.sql.SQLException
     */
    public void clearWarnings() throws SQLException
    {
        rs.clearWarnings();
    }
    /**
     * @throws java.sql.SQLException
     */
    public void close() throws SQLException
    {
    	// Libera todos os recursos do BD
        stmt.close();
        rs.close();
        listStatement.remove(stmt);
    }
    /**
     * @throws java.sql.SQLException
     */
    public void deleteRow() throws SQLException
    {
        rs.deleteRow();
    }
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        return rs.equals(obj);
    }
    /**
     * @param columnName
     * @return
     * @throws java.sql.SQLException
     */
    public int findColumn(String columnName) throws SQLException
    {
        return rs.findColumn(columnName);
    }
    /**
     * @return
     * @throws java.sql.SQLException
     */
    public boolean first() throws SQLException
    {
        return rs.first();
    }
    /**
     * @param i
     * @return
     * @throws java.sql.SQLException
     */
    public Array getArray(int i) throws SQLException
    {
        return rs.getArray(i);
    }
    /**
     * @param colName
     * @return
     * @throws java.sql.SQLException
     */
    public Array getArray(String colName) throws SQLException
    {
        return rs.getArray(colName);
    }
    /**
     * @param columnIndex
     * @return
     * @throws java.sql.SQLException
     */
    public InputStream getAsciiStream(int columnIndex) throws SQLException
    {
        return rs.getAsciiStream(columnIndex);
    }
    /**
     * @param columnName
     * @return
     * @throws java.sql.SQLException
     */
    public InputStream getAsciiStream(String columnName) throws SQLException
    {
        return rs.getAsciiStream(columnName);
    }
    /**
     * @param columnIndex
     * @return
     * @throws java.sql.SQLException
     */
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException
    {
        return rs.getBigDecimal(columnIndex);
    }
    /**
     * @param columnIndex
     * @param scale
     * @return
     * @throws java.sql.SQLException
     * @deprecated
     */
    public BigDecimal getBigDecimal(int columnIndex, int scale)
            throws SQLException
    {
        return rs.getBigDecimal(columnIndex, scale);
    }
    /**
     * @param columnName
     * @return
     * @throws java.sql.SQLException
     */
    public BigDecimal getBigDecimal(String columnName) throws SQLException
    {
        return rs.getBigDecimal(columnName);
    }
    /**
     * @param columnName
     * @param scale
     * @return
     * @throws java.sql.SQLException
     * @deprecated
     */
    public BigDecimal getBigDecimal(String columnName, int scale)
            throws SQLException
    {
        return rs.getBigDecimal(columnName, scale);
    }
    /**
     * @param columnIndex
     * @return
     * @throws java.sql.SQLException
     */
    public InputStream getBinaryStream(int columnIndex) throws SQLException
    {
        return rs.getBinaryStream(columnIndex);
    }
    /**
     * @param columnName
     * @return
     * @throws java.sql.SQLException
     */
    public InputStream getBinaryStream(String columnName) throws SQLException
    {
        return rs.getBinaryStream(columnName);
    }
    /**
     * @param i
     * @return
     * @throws java.sql.SQLException
     */
    public Blob getBlob(int i) throws SQLException
    {
        return rs.getBlob(i);
    }
    /**
     * @param colName
     * @return
     * @throws java.sql.SQLException
     */
    public Blob getBlob(String colName) throws SQLException
    {
        return rs.getBlob(colName);
    }
    /**
     * @param columnIndex
     * @return
     * @throws java.sql.SQLException
     */
    public boolean getBoolean(int columnIndex) throws SQLException
    {
        return rs.getBoolean(columnIndex);
    }
    /**
     * @param columnName
     * @return
     * @throws java.sql.SQLException
     */
    public boolean getBoolean(String columnName) throws SQLException
    {
        return rs.getBoolean(columnName);
    }
    /**
     * @param columnIndex
     * @return
     * @throws java.sql.SQLException
     */
    public byte getByte(int columnIndex) throws SQLException
    {
        return rs.getByte(columnIndex);
    }
    /**
     * @param columnName
     * @return
     * @throws java.sql.SQLException
     */
    public byte getByte(String columnName) throws SQLException
    {
        return rs.getByte(columnName);
    }
    /**
     * @param columnIndex
     * @return
     * @throws java.sql.SQLException
     */
    public byte[] getBytes(int columnIndex) throws SQLException
    {
        return rs.getBytes(columnIndex);
    }
    /**
     * @param columnName
     * @return
     * @throws java.sql.SQLException
     */
    public byte[] getBytes(String columnName) throws SQLException
    {
        return rs.getBytes(columnName);
    }
    /**
     * @param columnIndex
     * @return
     * @throws java.sql.SQLException
     */
    public Reader getCharacterStream(int columnIndex) throws SQLException
    {
        return rs.getCharacterStream(columnIndex);
    }
    /**
     * @param columnName
     * @return
     * @throws java.sql.SQLException
     */
    public Reader getCharacterStream(String columnName) throws SQLException
    {
        return rs.getCharacterStream(columnName);
    }
    /**
     * @param i
     * @return
     * @throws java.sql.SQLException
     */
    public Clob getClob(int i) throws SQLException
    {
        return rs.getClob(i);
    }
    /**
     * @param colName
     * @return
     * @throws java.sql.SQLException
     */
    public Clob getClob(String colName) throws SQLException
    {
        return rs.getClob(colName);
    }
    /**
     * @return
     * @throws java.sql.SQLException
     */
    public int getConcurrency() throws SQLException
    {
        return rs.getConcurrency();
    }
    /**
     * @return
     * @throws java.sql.SQLException
     */
    public String getCursorName() throws SQLException
    {
        return rs.getCursorName();
    }
    /**
     * @param columnIndex
     * @return
     * @throws java.sql.SQLException
     */
    public Date getDate(int columnIndex) throws SQLException
    {
        return rs.getDate(columnIndex);
    }
    /**
     * @param columnIndex
     * @param cal
     * @return
     * @throws java.sql.SQLException
     */
    public Date getDate(int columnIndex, Calendar cal) throws SQLException
    {
        return rs.getDate(columnIndex, cal);
    }
    /**
     * @param columnName
     * @return
     * @throws java.sql.SQLException
     */
    public Date getDate(String columnName) throws SQLException
    {
        return rs.getDate(columnName);
    }
    /**
     * @param columnName
     * @param cal
     * @return
     * @throws java.sql.SQLException
     */
    public Date getDate(String columnName, Calendar cal) throws SQLException
    {
        return rs.getDate(columnName, cal);
    }
    /**
     * @param columnIndex
     * @return
     * @throws java.sql.SQLException
     */
    public double getDouble(int columnIndex) throws SQLException
    {
        return rs.getDouble(columnIndex);
    }
    /**
     * @param columnName
     * @return
     * @throws java.sql.SQLException
     */
    public double getDouble(String columnName) throws SQLException
    {
        return rs.getDouble(columnName);
    }
    /**
     * @return
     * @throws java.sql.SQLException
     */
    public int getFetchDirection() throws SQLException
    {
        return rs.getFetchDirection();
    }
    /**
     * @return
     * @throws java.sql.SQLException
     */
    public int getFetchSize() throws SQLException
    {
        return rs.getFetchSize();
    }
    /**
     * @param columnIndex
     * @return
     * @throws java.sql.SQLException
     */
    public float getFloat(int columnIndex) throws SQLException
    {
        return rs.getFloat(columnIndex);
    }
    /**
     * @param columnName
     * @return
     * @throws java.sql.SQLException
     */
    public float getFloat(String columnName) throws SQLException
    {
        return rs.getFloat(columnName);
    }
    /**
     * @param columnIndex
     * @return
     * @throws java.sql.SQLException
     */
    public int getInt(int columnIndex) throws SQLException
    {
        return rs.getInt(columnIndex);
    }
    /**
     * @param columnName
     * @return
     * @throws java.sql.SQLException
     */
    public int getInt(String columnName) throws SQLException
    {
        return rs.getInt(columnName);
    }
    /**
     * @param columnIndex
     * @return
     * @throws java.sql.SQLException
     */
    public long getLong(int columnIndex) throws SQLException
    {
        return rs.getLong(columnIndex);
    }
    /**
     * @param columnName
     * @return
     * @throws java.sql.SQLException
     */
    public long getLong(String columnName) throws SQLException
    {
        return rs.getLong(columnName);
    }
    /**
     * @return
     * @throws java.sql.SQLException
     */
    public ResultSetMetaData getMetaData() throws SQLException
    {
        return rs.getMetaData();
    }
    /**
     * @param columnIndex
     * @return
     * @throws java.sql.SQLException
     */
    public Object getObject(int columnIndex) throws SQLException
    {
        return rs.getObject(columnIndex);
    }
    /**
     * @param i
     * @param map
     * @return
     * @throws java.sql.SQLException
     */
    public Object getObject(int i, Map map) throws SQLException
    {
        return rs.getObject(i, map);
    }
    /**
     * @param columnName
     * @return
     * @throws java.sql.SQLException
     */
    public Object getObject(String columnName) throws SQLException
    {
        return rs.getObject(columnName);
    }
    /**
     * @param colName
     * @param map
     * @return
     * @throws java.sql.SQLException
     */
    public Object getObject(String colName, Map map) throws SQLException
    {
        return rs.getObject(colName, map);
    }
    /**
     * @param i
     * @return
     * @throws java.sql.SQLException
     */
    public Ref getRef(int i) throws SQLException
    {
        return rs.getRef(i);
    }
    /**
     * @param colName
     * @return
     * @throws java.sql.SQLException
     */
    public Ref getRef(String colName) throws SQLException
    {
        return rs.getRef(colName);
    }
    /**
     * @return
     * @throws java.sql.SQLException
     */
    public int getRow() throws SQLException
    {
        return rs.getRow();
    }
    /**
     * @param columnIndex
     * @return
     * @throws java.sql.SQLException
     */
    public short getShort(int columnIndex) throws SQLException
    {
        return rs.getShort(columnIndex);
    }
    /**
     * @param columnName
     * @return
     * @throws java.sql.SQLException
     */
    public short getShort(String columnName) throws SQLException
    {
        return rs.getShort(columnName);
    }
    /**
     * @return
     * @throws java.sql.SQLException
     */
    public Statement getStatement() throws SQLException
    {
        return rs.getStatement();
    }
    /**
     * @param columnIndex
     * @return
     * @throws java.sql.SQLException
     */
    public String getString(int columnIndex) throws SQLException
    {
        return rs.getString(columnIndex);
    }
    /**
     * @param columnName
     * @return
     * @throws java.sql.SQLException
     */
    public String getString(String columnName) throws SQLException
    {
        return rs.getString(columnName);
    }
    /**
     * @param columnIndex
     * @return
     * @throws java.sql.SQLException
     */
    public Time getTime(int columnIndex) throws SQLException
    {
        return rs.getTime(columnIndex);
    }
    /**
     * @param columnIndex
     * @param cal
     * @return
     * @throws java.sql.SQLException
     */
    public Time getTime(int columnIndex, Calendar cal) throws SQLException
    {
        return rs.getTime(columnIndex, cal);
    }
    /**
     * @param columnName
     * @return
     * @throws java.sql.SQLException
     */
    public Time getTime(String columnName) throws SQLException
    {
        return rs.getTime(columnName);
    }
    /**
     * @param columnName
     * @param cal
     * @return
     * @throws java.sql.SQLException
     */
    public Time getTime(String columnName, Calendar cal) throws SQLException
    {
        return rs.getTime(columnName, cal);
    }
    /**
     * @param columnIndex
     * @return
     * @throws java.sql.SQLException
     */
    public Timestamp getTimestamp(int columnIndex) throws SQLException
    {
        return rs.getTimestamp(columnIndex);
    }
    /**
     * @param columnIndex
     * @param cal
     * @return
     * @throws java.sql.SQLException
     */
    public Timestamp getTimestamp(int columnIndex, Calendar cal)
            throws SQLException
    {
        return rs.getTimestamp(columnIndex, cal);
    }
    /**
     * @param columnName
     * @return
     * @throws java.sql.SQLException
     */
    public Timestamp getTimestamp(String columnName) throws SQLException
    {
        return rs.getTimestamp(columnName);
    }
    /**
     * @param columnName
     * @param cal
     * @return
     * @throws java.sql.SQLException
     */
    public Timestamp getTimestamp(String columnName, Calendar cal)
            throws SQLException
    {
        return rs.getTimestamp(columnName, cal);
    }
    /**
     * @return
     * @throws java.sql.SQLException
     */
    public int getType() throws SQLException
    {
        return rs.getType();
    }
    /**
     * @param columnIndex
     * @return
     * @throws java.sql.SQLException
     * @deprecated
     */
    public InputStream getUnicodeStream(int columnIndex) throws SQLException
    {
        return rs.getUnicodeStream(columnIndex);
    }
    /**
     * @param columnName
     * @return
     * @throws java.sql.SQLException
     * @deprecated
     */
    public InputStream getUnicodeStream(String columnName) throws SQLException
    {
        return rs.getUnicodeStream(columnName);
    }
    /**
     * @param columnIndex
     * @return
     * @throws java.sql.SQLException
     */
    public URL getURL(int columnIndex) throws SQLException
    {
        return rs.getURL(columnIndex);
    }
    /**
     * @param columnName
     * @return
     * @throws java.sql.SQLException
     */
    public URL getURL(String columnName) throws SQLException
    {
        return rs.getURL(columnName);
    }
    /**
     * @return
     * @throws java.sql.SQLException
     */
    public SQLWarning getWarnings() throws SQLException
    {
        return rs.getWarnings();
    }
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return rs.hashCode();
    }
    /**
     * @throws java.sql.SQLException
     */
    public void insertRow() throws SQLException
    {
        rs.insertRow();
    }
    /**
     * @return
     * @throws java.sql.SQLException
     */
    public boolean isAfterLast() throws SQLException
    {
        return rs.isAfterLast();
    }
    /**
     * @return
     * @throws java.sql.SQLException
     */
    public boolean isBeforeFirst() throws SQLException
    {
        return rs.isBeforeFirst();
    }
    /**
     * @return
     * @throws java.sql.SQLException
     */
    public boolean isFirst() throws SQLException
    {
        return rs.isFirst();
    }
    /**
     * @return
     * @throws java.sql.SQLException
     */
    public boolean isLast() throws SQLException
    {
        return rs.isLast();
    }
    /**
     * @return
     * @throws java.sql.SQLException
     */
    public boolean last() throws SQLException
    {
        return rs.last();
    }
    /**
     * @throws java.sql.SQLException
     */
    public void moveToCurrentRow() throws SQLException
    {
        rs.moveToCurrentRow();
    }
    /**
     * @throws java.sql.SQLException
     */
    public void moveToInsertRow() throws SQLException
    {
        rs.moveToInsertRow();
    }
    /**
     * @return
     * @throws java.sql.SQLException
     */
    public boolean next() throws SQLException
    {
        return rs.next();
    }
    /**
     * @return
     * @throws java.sql.SQLException
     */
    public boolean previous() throws SQLException
    {
        return rs.previous();
    }
    /**
     * @throws java.sql.SQLException
     */
    public void refreshRow() throws SQLException
    {
        rs.refreshRow();
    }
    /**
     * @param rows
     * @return
     * @throws java.sql.SQLException
     */
    public boolean relative(int rows) throws SQLException
    {
        return rs.relative(rows);
    }
    /**
     * @return
     * @throws java.sql.SQLException
     */
    public boolean rowDeleted() throws SQLException
    {
        return rs.rowDeleted();
    }
    /**
     * @return
     * @throws java.sql.SQLException
     */
    public boolean rowInserted() throws SQLException
    {
        return rs.rowInserted();
    }
    /**
     * @return
     * @throws java.sql.SQLException
     */
    public boolean rowUpdated() throws SQLException
    {
        return rs.rowUpdated();
    }
    /**
     * @param direction
     * @throws java.sql.SQLException
     */
    public void setFetchDirection(int direction) throws SQLException
    {
        rs.setFetchDirection(direction);
    }
    /**
     * @param rows
     * @throws java.sql.SQLException
     */
    public void setFetchSize(int rows) throws SQLException
    {
        rs.setFetchSize(rows);
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return rs.toString();
    }
    /**
     * @param columnIndex
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateArray(int columnIndex, Array x) throws SQLException
    {
        rs.updateArray(columnIndex, x);
    }
    /**
     * @param columnName
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateArray(String columnName, Array x) throws SQLException
    {
        rs.updateArray(columnName, x);
    }
    /**
     * @param columnIndex
     * @param x
     * @param length
     * @throws java.sql.SQLException
     */
    public void updateAsciiStream(int columnIndex, InputStream x, int length)
            throws SQLException
    {
        rs.updateAsciiStream(columnIndex, x, length);
    }
    /**
     * @param columnName
     * @param x
     * @param length
     * @throws java.sql.SQLException
     */
    public void updateAsciiStream(String columnName, InputStream x, int length)
            throws SQLException
    {
        rs.updateAsciiStream(columnName, x, length);
    }
    /**
     * @param columnIndex
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateBigDecimal(int columnIndex, BigDecimal x)
            throws SQLException
    {
        rs.updateBigDecimal(columnIndex, x);
    }
    /**
     * @param columnName
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateBigDecimal(String columnName, BigDecimal x)
            throws SQLException
    {
        rs.updateBigDecimal(columnName, x);
    }
    /**
     * @param columnIndex
     * @param x
     * @param length
     * @throws java.sql.SQLException
     */
    public void updateBinaryStream(int columnIndex, InputStream x, int length)
            throws SQLException
    {
        rs.updateBinaryStream(columnIndex, x, length);
    }
    /**
     * @param columnName
     * @param x
     * @param length
     * @throws java.sql.SQLException
     */
    public void updateBinaryStream(String columnName, InputStream x, int length)
            throws SQLException
    {
        rs.updateBinaryStream(columnName, x, length);
    }
    /**
     * @param columnIndex
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateBlob(int columnIndex, Blob x) throws SQLException
    {
        rs.updateBlob(columnIndex, x);
    }
    /**
     * @param columnName
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateBlob(String columnName, Blob x) throws SQLException
    {
        rs.updateBlob(columnName, x);
    }
    /**
     * @param columnIndex
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateBoolean(int columnIndex, boolean x) throws SQLException
    {
        rs.updateBoolean(columnIndex, x);
    }
    /**
     * @param columnName
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateBoolean(String columnName, boolean x) throws SQLException
    {
        rs.updateBoolean(columnName, x);
    }
    /**
     * @param columnIndex
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateByte(int columnIndex, byte x) throws SQLException
    {
        rs.updateByte(columnIndex, x);
    }
    /**
     * @param columnName
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateByte(String columnName, byte x) throws SQLException
    {
        rs.updateByte(columnName, x);
    }
    /**
     * @param columnIndex
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateBytes(int columnIndex, byte[] x) throws SQLException
    {
        rs.updateBytes(columnIndex, x);
    }
    /**
     * @param columnName
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateBytes(String columnName, byte[] x) throws SQLException
    {
        rs.updateBytes(columnName, x);
    }
    /**
     * @param columnIndex
     * @param x
     * @param length
     * @throws java.sql.SQLException
     */
    public void updateCharacterStream(int columnIndex, Reader x, int length)
            throws SQLException
    {
        rs.updateCharacterStream(columnIndex, x, length);
    }
    /**
     * @param columnName
     * @param reader
     * @param length
     * @throws java.sql.SQLException
     */
    public void updateCharacterStream(String columnName, Reader reader,
            int length) throws SQLException
    {
        rs.updateCharacterStream(columnName, reader, length);
    }
    /**
     * @param columnIndex
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateClob(int columnIndex, Clob x) throws SQLException
    {
        rs.updateClob(columnIndex, x);
    }
    /**
     * @param columnName
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateClob(String columnName, Clob x) throws SQLException
    {
        rs.updateClob(columnName, x);
    }
    /**
     * @param columnIndex
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateDate(int columnIndex, Date x) throws SQLException
    {
        rs.updateDate(columnIndex, x);
    }
    /**
     * @param columnName
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateDate(String columnName, Date x) throws SQLException
    {
        rs.updateDate(columnName, x);
    }
    /**
     * @param columnIndex
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateDouble(int columnIndex, double x) throws SQLException
    {
        rs.updateDouble(columnIndex, x);
    }
    /**
     * @param columnName
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateDouble(String columnName, double x) throws SQLException
    {
        rs.updateDouble(columnName, x);
    }
    /**
     * @param columnIndex
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateFloat(int columnIndex, float x) throws SQLException
    {
        rs.updateFloat(columnIndex, x);
    }
    /**
     * @param columnName
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateFloat(String columnName, float x) throws SQLException
    {
        rs.updateFloat(columnName, x);
    }
    /**
     * @param columnIndex
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateInt(int columnIndex, int x) throws SQLException
    {
        rs.updateInt(columnIndex, x);
    }
    /**
     * @param columnName
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateInt(String columnName, int x) throws SQLException
    {
        rs.updateInt(columnName, x);
    }
    /**
     * @param columnIndex
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateLong(int columnIndex, long x) throws SQLException
    {
        rs.updateLong(columnIndex, x);
    }
    /**
     * @param columnName
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateLong(String columnName, long x) throws SQLException
    {
        rs.updateLong(columnName, x);
    }
    /**
     * @param columnIndex
     * @throws java.sql.SQLException
     */
    public void updateNull(int columnIndex) throws SQLException
    {
        rs.updateNull(columnIndex);
    }
    /**
     * @param columnName
     * @throws java.sql.SQLException
     */
    public void updateNull(String columnName) throws SQLException
    {
        rs.updateNull(columnName);
    }
    /**
     * @param columnIndex
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateObject(int columnIndex, Object x) throws SQLException
    {
        rs.updateObject(columnIndex, x);
    }
    /**
     * @param columnIndex
     * @param x
     * @param scale
     * @throws java.sql.SQLException
     */
    public void updateObject(int columnIndex, Object x, int scale)
            throws SQLException
    {
        rs.updateObject(columnIndex, x, scale);
    }
    /**
     * @param columnName
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateObject(String columnName, Object x) throws SQLException
    {
        rs.updateObject(columnName, x);
    }
    /**
     * @param columnName
     * @param x
     * @param scale
     * @throws java.sql.SQLException
     */
    public void updateObject(String columnName, Object x, int scale)
            throws SQLException
    {
        rs.updateObject(columnName, x, scale);
    }
    /**
     * @param columnIndex
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateRef(int columnIndex, Ref x) throws SQLException
    {
        rs.updateRef(columnIndex, x);
    }
    /**
     * @param columnName
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateRef(String columnName, Ref x) throws SQLException
    {
        rs.updateRef(columnName, x);
    }
    /**
     * @throws java.sql.SQLException
     */
    public void updateRow() throws SQLException
    {
        rs.updateRow();
    }
    /**
     * @param columnIndex
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateShort(int columnIndex, short x) throws SQLException
    {
        rs.updateShort(columnIndex, x);
    }
    /**
     * @param columnName
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateShort(String columnName, short x) throws SQLException
    {
        rs.updateShort(columnName, x);
    }
    /**
     * @param columnIndex
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateString(int columnIndex, String x) throws SQLException
    {
        rs.updateString(columnIndex, x);
    }
    /**
     * @param columnName
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateString(String columnName, String x) throws SQLException
    {
        rs.updateString(columnName, x);
    }
    /**
     * @param columnIndex
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateTime(int columnIndex, Time x) throws SQLException
    {
        rs.updateTime(columnIndex, x);
    }
    /**
     * @param columnName
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateTime(String columnName, Time x) throws SQLException
    {
        rs.updateTime(columnName, x);
    }
    /**
     * @param columnIndex
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateTimestamp(int columnIndex, Timestamp x)
            throws SQLException
    {
        rs.updateTimestamp(columnIndex, x);
    }
    /**
     * @param columnName
     * @param x
     * @throws java.sql.SQLException
     */
    public void updateTimestamp(String columnName, Timestamp x)
            throws SQLException
    {
        rs.updateTimestamp(columnName, x);
    }
    /**
     * @return
     * @throws java.sql.SQLException
     */
    public boolean wasNull() throws SQLException
    {
        return rs.wasNull();
    }
}
