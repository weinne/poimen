package br.com.poimen.repository.search;

import br.com.poimen.domain.ApplicationUser;
import br.com.poimen.repository.ApplicationUserRepository;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link ApplicationUser} entity.
 */
public interface ApplicationUserSearchRepository
    extends ElasticsearchRepository<ApplicationUser, Long>, ApplicationUserSearchRepositoryInternal {}

interface ApplicationUserSearchRepositoryInternal {
    Stream<ApplicationUser> search(String query);

    Stream<ApplicationUser> search(Query query);

    @Async
    void index(ApplicationUser entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ApplicationUserSearchRepositoryInternalImpl implements ApplicationUserSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ApplicationUserRepository repository;

    ApplicationUserSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ApplicationUserRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<ApplicationUser> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<ApplicationUser> search(Query query) {
        return elasticsearchTemplate.search(query, ApplicationUser.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(ApplicationUser entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ApplicationUser.class);
    }
}
