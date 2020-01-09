/*
 * This file is generated by jOOQ.
 */
package io.skymind.pathmind.data.db;


import io.skymind.pathmind.data.db.tables.ExecutionProviderMetaData;
import io.skymind.pathmind.data.db.tables.Experiment;
import io.skymind.pathmind.data.db.tables.Model;
import io.skymind.pathmind.data.db.tables.PathmindUser;
import io.skymind.pathmind.data.db.tables.Policy;
import io.skymind.pathmind.data.db.tables.Project;
import io.skymind.pathmind.data.db.tables.Run;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.Generated;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Public extends SchemaImpl {

    private static final long serialVersionUID = -1455313339;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * The table <code>public.execution_provider_meta_data</code>.
     */
    public final ExecutionProviderMetaData EXECUTION_PROVIDER_META_DATA = io.skymind.pathmind.data.db.tables.ExecutionProviderMetaData.EXECUTION_PROVIDER_META_DATA;

    /**
     * The table <code>public.experiment</code>.
     */
    public final Experiment EXPERIMENT = io.skymind.pathmind.data.db.tables.Experiment.EXPERIMENT;

    /**
     * The table <code>public.model</code>.
     */
    public final Model MODEL = io.skymind.pathmind.data.db.tables.Model.MODEL;

    /**
     * The table <code>public.pathmind_user</code>.
     */
    public final PathmindUser PATHMIND_USER = io.skymind.pathmind.data.db.tables.PathmindUser.PATHMIND_USER;

    /**
     * The table <code>public.policy</code>.
     */
    public final Policy POLICY = io.skymind.pathmind.data.db.tables.Policy.POLICY;

    /**
     * The table <code>public.project</code>.
     */
    public final Project PROJECT = io.skymind.pathmind.data.db.tables.Project.PROJECT;

    /**
     * The table <code>public.run</code>.
     */
    public final Run RUN = io.skymind.pathmind.data.db.tables.Run.RUN;

    /**
     * No further instances allowed
     */
    private Public() {
        super("public", null);
    }


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
            ExecutionProviderMetaData.EXECUTION_PROVIDER_META_DATA,
            Experiment.EXPERIMENT,
            Model.MODEL,
            PathmindUser.PATHMIND_USER,
            Policy.POLICY,
            Project.PROJECT,
            Run.RUN);
    }
}
