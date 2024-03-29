/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.db.jooq.tables;


import io.skymind.pathmind.db.jooq.Keys;
import io.skymind.pathmind.db.jooq.Public;
import io.skymind.pathmind.db.jooq.tables.records.RunAdminNoteRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row2;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RunAdminNote extends TableImpl<RunAdminNoteRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.run_admin_note</code>
     */
    public static final RunAdminNote RUN_ADMIN_NOTE = new RunAdminNote();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RunAdminNoteRecord> getRecordType() {
        return RunAdminNoteRecord.class;
    }

    /**
     * The column <code>public.run_admin_note.run_id</code>.
     */
    public final TableField<RunAdminNoteRecord, Long> RUN_ID = createField(DSL.name("run_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.run_admin_note.note</code>.
     */
    public final TableField<RunAdminNoteRecord, String> NOTE = createField(DSL.name("note"), SQLDataType.CLOB, this, "");

    private RunAdminNote(Name alias, Table<RunAdminNoteRecord> aliased) {
        this(alias, aliased, null);
    }

    private RunAdminNote(Name alias, Table<RunAdminNoteRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.run_admin_note</code> table reference
     */
    public RunAdminNote(String alias) {
        this(DSL.name(alias), RUN_ADMIN_NOTE);
    }

    /**
     * Create an aliased <code>public.run_admin_note</code> table reference
     */
    public RunAdminNote(Name alias) {
        this(alias, RUN_ADMIN_NOTE);
    }

    /**
     * Create a <code>public.run_admin_note</code> table reference
     */
    public RunAdminNote() {
        this(DSL.name("run_admin_note"), null);
    }

    public <O extends Record> RunAdminNote(Table<O> child, ForeignKey<O, RunAdminNoteRecord> key) {
        super(child, key, RUN_ADMIN_NOTE);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<UniqueKey<RunAdminNoteRecord>> getKeys() {
        return Arrays.<UniqueKey<RunAdminNoteRecord>>asList(Keys.PM_RUN_ADMIN_NOTES_RUN_ID_UNQ);
    }

    @Override
    public List<ForeignKey<RunAdminNoteRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<RunAdminNoteRecord, ?>>asList(Keys.RUN_ADMIN_NOTE__PM_FK_RUN_ADMIN_NOTES);
    }

    private transient Run _run;

    public Run run() {
        if (_run == null)
            _run = new Run(this, Keys.RUN_ADMIN_NOTE__PM_FK_RUN_ADMIN_NOTES);

        return _run;
    }

    @Override
    public RunAdminNote as(String alias) {
        return new RunAdminNote(DSL.name(alias), this);
    }

    @Override
    public RunAdminNote as(Name alias) {
        return new RunAdminNote(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public RunAdminNote rename(String name) {
        return new RunAdminNote(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public RunAdminNote rename(Name name) {
        return new RunAdminNote(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<Long, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}
