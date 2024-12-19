package br.com.poimen.repository.search;

import br.com.poimen.domain.Member;
import br.com.poimen.repository.MemberRepository;
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
 * Spring Data Elasticsearch repository for the {@link Member} entity.
 */
public interface MemberSearchRepository extends ElasticsearchRepository<Member, Long>, MemberSearchRepositoryInternal {}

interface MemberSearchRepositoryInternal {
    Page<Member> search(String query, Pageable pageable);

    Page<Member> search(Query query);

    @Async
    void index(Member entity);

    @Async
    void deleteFromIndexById(Long id);
}

class MemberSearchRepositoryInternalImpl implements MemberSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final MemberRepository repository;

    MemberSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, MemberRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Member> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Member> search(Query query) {
        SearchHits<Member> searchHits = elasticsearchTemplate.search(query, Member.class);
        List<Member> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Member entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Member.class);
    }
}
