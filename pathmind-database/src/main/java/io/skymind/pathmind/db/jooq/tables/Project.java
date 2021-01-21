/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables;


import io.skymind.pathmind.db.jooq.Indexes;
import io.skymind.pathmind.db.jooq.Keys;
import io.skymind.pathmind.db.jooq.Public;
import io.skymind.pathmind.db.jooq.tables.records.ProjectRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row7;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Project extends TableImpl<ProjectRecord> {

    private static final long serialVersionUID = 698437897;

    /**
     * The reference instance of <code>public.project</code>
     */
    public static final Project PROJECT = new Project();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ProjectRecord> getRecordType() {
        return ProjectRecord.class;
    }

    /**
     * The column <code>public.project.id</code>.
     */
    public final TableField<ProjectRecord, Long> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.project.pathmind_user_id</code>.
     */
    public final TableField<ProjectRecord, Long> PATHMIND_USER_ID = createField(DSL.name("pathmind_user_id"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.project.name</code>.
     */
    public final TableField<ProjectRecord, String> NAME = createField(DSL.name("name"), org.jooq.impl.SQLDataType.VARCHAR(100).nullable(false), this, "");

    /**
     * The column <code>public.project.date_created</code>.
     */
    public final TableField<ProjectRecord, LocalDateTime> DATE_CREATED = createField(DSL.name("date_created"), org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false).defaultValue(org.jooq.impl.DSL.field("now()", org.jooq.impl.SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>public.project.last_activity_date</code>.
     */
    public final TableField<ProjectRecord, LocalDateTime> LAST_ACTIVITY_DATE = createField(DSL.name("last_activity_date"), org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false).defaultValue(org.jooq.impl.DSL.field("now()", org.jooq.impl.SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>public.project.archived</code>.
     */
    public final TableField<ProjectRecord, Boolean> ARCHIVED = createField(DSL.name("archived"), org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaultValue(org.jooq.impl.DSL.field("false", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.project.user_notes</code>.
     */
    public final TableField<ProjectRecord, String> USER_NOTES = createField(DSL.name("user_notes"), org.jooq.impl.SQLDataType.VARCHAR(1000).nullable(false).defaultValue(org.jooq.impl.DSL.field("''::character varying", org.jooq.impl.SQLDataType.VARCHAR)), this, "");

    /**
     * Create a <code>public.project</code> table reference
     */
    public Project() {
        this(DSL.name("project"), null);
    }

    /**
     * Create an aliased <code>public.project</code> table reference
     */
    public Project(String alias) {
        this(DSL.name(alias), PROJECT);
    }

    /**
     * Create an aliased <code>public.project</code> table reference
     */
    public Project(Name alias) {
        this(alias, PROJECT);
    }

    private Project(Name alias, Table<ProjectRecord> aliased) {
        this(alias, aliased, null);
    }

    private Project(Name alias, Table<ProjectRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Project(Table<O> child, ForeignKey<O, ProjectRecord> key) {
        super(child, key, PROJECT);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.PROJECT_PATHMIND_USER_FK_INDEX, Indexes.PROJECT_PKEY, Indexes.UNIQUE_PROJECT_NAME_PATHMIND_USER_ID);
    }

    @Override
    public UniqueKey<ProjectRecord> getPrimaryKey() {
        return Keys.PROJECT_PKEY;
    }

    @Override
    public List<UniqueKey<ProjectRecord>> getKeys() {
        return Arrays.<UniqueKey<ProjectRecord>>asList(Keys.PROJECT_PKEY, Keys.UNIQUE_PROJECT_NAME_PATHMIND_USER_ID);
    }

    @Override
    public List<ForeignKey<ProjectRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<ProjectRecord, ?>>asList(Keys.PROJECT__PM_FK_PROJECT_PATHMIND_USER);
    }

    public PathmindUser pathmindUser() {
        return new PathmindUser(this, Keys.PROJECT__PM_FK_PROJECT_PATHMIND_USER);
    }

    @Override
    public Project as(String alias) {
        return new Project(DSL.name(alias), this);
    }

    @Override
    public Project as(Name alias) {
        return new Project(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Project rename(String name) {
        return new Project(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Project rename(Name name) {
        return new Project(name, null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<Long, Long, String, LocalDateTime, LocalDateTime, Boolean, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }
}
