package org.anhcraft.spaciouslib.database;

import org.anhcraft.spaciouslib.utils.TaskStatus;

import java.sql.ResultSet;

public abstract class QueryTaskResult {
    public abstract TaskStatus result(ResultSet result);
}
