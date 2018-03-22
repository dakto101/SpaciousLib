package org.anhcraft.spaciouslib.database;

import java.sql.ResultSet;

public abstract class QueryTaskResult {
    public abstract Database.TaskStatus result(ResultSet result);
}
