package br.com.poimen.repository;

import br.com.poimen.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Member entity.
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    default Optional<Member> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Member> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Member> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select member from Member member left join fetch member.church", countQuery = "select count(member) from Member member")
    Page<Member> findAllWithToOneRelationships(Pageable pageable);

    @Query("select member from Member member left join fetch member.church")
    List<Member> findAllWithToOneRelationships();

    @Query("select member from Member member left join fetch member.church where member.id =:id")
    Optional<Member> findOneWithToOneRelationships(@Param("id") Long id);
}
