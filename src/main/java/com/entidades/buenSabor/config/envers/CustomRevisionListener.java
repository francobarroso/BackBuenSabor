package com.entidades.buenSabor.config.envers;

import com.entidades.buenSabor.audit.Revision;
import org.hibernate.envers.RevisionListener;

public class CustomRevisionListener implements RevisionListener {
    public void newRevision(Object revisionEntity) {
        final Revision revision = (Revision) revisionEntity;
    }
}
