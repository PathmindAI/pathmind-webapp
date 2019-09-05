/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.data.db;


import io.skymind.pathmind.data.db.tables.Experiment;
import io.skymind.pathmind.data.db.tables.PathmindUser;
import io.skymind.pathmind.data.db.tables.Project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Public extends SchemaImpl {

    private static final long serialVersionUID = -707317019;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * The table <code>public.experiment</code>.
     */
    public final Experiment EXPERIMENT = io.skymind.pathmind.data.db.tables.Experiment.EXPERIMENT;

    /**
     * The table <code>public.pathmind_user</code>.
     */
    public final PathmindUser PATHMIND_USER = io.skymind.pathmind.data.db.tables.PathmindUser.PATHMIND_USER;

    /**
     * The table <code>public.project</code>.
     */
    public final Project PROJECT = io.skymind.pathmind.data.db.tables.Project.PROJECT;

    /**
     * No further instances allowed
     */
    private Public() {
        super("public", null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        List result = new ArrayList();
        result.addAll(getTables0());
        return result;
    }

    private final List<Table<?>> getTables0() {
        return Arrays.<Table<?>>asList(
            Experiment.EXPERIMENT,
            PathmindUser.PATHMIND_USER,
            Project.PROJECT);
    }
}
