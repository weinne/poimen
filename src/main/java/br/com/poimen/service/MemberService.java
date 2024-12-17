package br.com.poimen.service;

import br.com.poimen.domain.Member;
import br.com.poimen.repository.MemberRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.poimen.domain.Member}.
 */
@Service
@Transactional
public class MemberService {

    private static final Logger LOG = LoggerFactory.getLogger(MemberService.class);

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * Save a member.
     *
     * @param member the entity to save.
     * @return the persisted entity.
     */
    public Member save(Member member) {
        LOG.debug("Request to save Member : {}", member);
        return memberRepository.save(member);
    }

    /**
     * Update a member.
     *
     * @param member the entity to save.
     * @return the persisted entity.
     */
    public Member update(Member member) {
        LOG.debug("Request to update Member : {}", member);
        return memberRepository.save(member);
    }

    /**
     * Partially update a member.
     *
     * @param member the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Member> partialUpdate(Member member) {
        LOG.debug("Request to partially update Member : {}", member);

        return memberRepository
            .findById(member.getId())
            .map(existingMember -> {
                if (member.getFirstName() != null) {
                    existingMember.setFirstName(member.getFirstName());
                }
                if (member.getLastName() != null) {
                    existingMember.setLastName(member.getLastName());
                }
                if (member.getEmail() != null) {
                    existingMember.setEmail(member.getEmail());
                }
                if (member.getPhoneNumber() != null) {
                    existingMember.setPhoneNumber(member.getPhoneNumber());
                }
                if (member.getDateOfBirth() != null) {
                    existingMember.setDateOfBirth(member.getDateOfBirth());
                }
                if (member.getAddress() != null) {
                    existingMember.setAddress(member.getAddress());
                }

                return existingMember;
            })
            .map(memberRepository::save);
    }

    /**
     * Get all the members.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Member> findAll(Pageable pageable) {
        LOG.debug("Request to get all Members");
        return memberRepository.findAll(pageable);
    }

    /**
     * Get one member by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Member> findOne(Long id) {
        LOG.debug("Request to get Member : {}", id);
        return memberRepository.findById(id);
    }

    /**
     * Delete the member by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Member : {}", id);
        memberRepository.deleteById(id);
    }
}
