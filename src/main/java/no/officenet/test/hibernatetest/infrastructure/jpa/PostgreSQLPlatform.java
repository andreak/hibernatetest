package no.officenet.test.hibernatetest.infrastructure.jpa;

import org.eclipse.persistence.internal.sessions.AbstractSession;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class PostgreSQLPlatform extends org.eclipse.persistence.platform.database.PostgreSQLPlatform {

	@Override
	public Object getObjectFromResultSet(ResultSet resultSet, int columnNumber, int type, AbstractSession session) throws SQLException {
		if ((type == Types.BIGINT || type == Types.INTEGER) && resultSet.getMetaData().getColumnTypeName(columnNumber).equals("oid")) {
			return resultSet.getBlob(columnNumber);
		} else {
			return super.getObjectFromResultSet(resultSet, columnNumber, type, session);
		}
	}
}
