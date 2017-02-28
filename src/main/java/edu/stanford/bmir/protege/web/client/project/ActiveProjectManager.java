package edu.stanford.bmir.protege.web.client.project;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20/12/15
 */
public interface ActiveProjectManager {

    /**
     * Gets the {@link ProjectId} of the active project.
     * @return The {@link ProjectId} of the active project.
     */
    @Nonnull
    Optional<ProjectId> getActiveProjectId();

}
