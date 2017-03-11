package edu.stanford.bmir.protege.web.server.place;

import edu.stanford.bmir.protege.web.client.place.ItemSelection;
import edu.stanford.bmir.protege.web.server.inject.ApplicationHost;
import edu.stanford.bmir.protege.web.server.inject.ApplicationName;
import edu.stanford.bmir.protege.web.server.inject.ApplicationPath;
import edu.stanford.bmir.protege.web.server.perspective.EntityTypePerspectiveMapper;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlaceTokenizer;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Mar 2017
 */
public class PlaceUrl {

    private static final PerspectiveId DEFAULT_PERSPECTIVE = new PerspectiveId("Classes");

    private final String applicationHost;

    private final String applicationPath;

    private final String applicationName;

    private final EntityTypePerspectiveMapper mapper;

    @Inject
    public PlaceUrl(@Nonnull @ApplicationHost String applicationHost,
                    @Nonnull @ApplicationPath String applicationPath,
                    @Nonnull @ApplicationName String applicationName,
                    @Nonnull EntityTypePerspectiveMapper mapper) {
        this.applicationHost = checkNotNull(applicationHost);
        this.applicationPath = checkNotNull(applicationPath);
        this.applicationName = checkNotNull(applicationName);
        this.mapper = checkNotNull(mapper);
    }

    /**
     * Gets the application Url.  This is the root Url of the WebProtege application.
     * @return A string representing the Url.
     */
    @Nonnull
    public String getApplicationUrl() {
        return createUrl(null);
    }

    /**
     * Gets the application anchor.  This is the name of the WebProtege application surrounded by
     * anchor tags whose href is the value provided by {@link #getApplicationUrl()}.
     * @return A string representing the Url.
     */
    @Nonnull
    public String getApplicationAnchor() {
        return String.format("<a href=\"%s\">%s</a>", applicationName, getApplicationUrl());
    }

    /**
     * Get the Url for the specified project.
     * @param projectId The project whose Url will be returned.
     * @return A string representing the Url.
     */
    @Nonnull
    public String getProjectUrl(@Nonnull ProjectId projectId) {
        ProjectViewPlace place = new ProjectViewPlace(projectId, DEFAULT_PERSPECTIVE, ItemSelection.empty());
        String projectPlaceFragment = getProjectPlaceFragment(place);
        return createUrl(projectPlaceFragment);
    }

    /**
     * Gets the Url for the specified entity in the specified project.
     * @param projectId The project.
     * @param entity The entity.
     * @return A string representing the Url.
     */
    @Nonnull
    public String getEntityUrl(@Nonnull ProjectId projectId,
                               @Nonnull OWLEntity entity) {
        ProjectViewPlace place = new ProjectViewPlace(projectId,
                                                      mapper.getPerspectiveId(entity.getEntityType()),
                                                      ItemSelection.builder().addEntity(entity).build());
        String projectPlaceFragment = getProjectPlaceFragment(place);
        return createUrl(projectPlaceFragment);

    }

    private String getProjectPlaceFragment(ProjectViewPlace place) {
        ProjectViewPlaceTokenizer tokenizer = new ProjectViewPlaceTokenizer();
        String placeToken = tokenizer.getToken(place);
        return "ProjectViewPlace:" + placeToken;
    }


    private String createUrl(@Nullable String fragment) {
        try {
            return new URI("http" , applicationHost, applicationPath, fragment).toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}