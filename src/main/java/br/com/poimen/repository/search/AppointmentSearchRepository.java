package br.com.poimen.repository.search;

import br.com.poimen.domain.Appointment;
import br.com.poimen.repository.AppointmentRepository;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Appointment} entity.
 */
public interface AppointmentSearchRepository extends ElasticsearchRepository<Appointment, Long>, AppointmentSearchRepositoryInternal {}

interface AppointmentSearchRepositoryInternal {
    Page<Appointment> search(String query, Pageable pageable);

    Page<Appointment> search(Query query);

    @Async
    void index(Appointment entity);

    @Async
    void deleteFromIndexById(Long id);
}

class AppointmentSearchRepositoryInternalImpl implements AppointmentSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AppointmentRepository repository;

    AppointmentSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, AppointmentRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Appointment> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Appointment> search(Query query) {
        SearchHits<Appointment> searchHits = elasticsearchTemplate.search(query, Appointment.class);
        List<Appointment> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Appointment entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Appointment.class);
    }
}
