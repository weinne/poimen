package br.com.poimen.service;

import br.com.poimen.domain.Member;
import br.com.poimen.repository.MemberRepository;
import br.com.poimen.repository.search.MemberSearchRepository;
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

    private final MemberSearchRepository memberSearchRepository;

    public MemberService(MemberRepository memberRepository, MemberSearchRepository memberSearchRepository) {
        this.memberRepository = memberRepository;
        this.memberSearchRepository = memberSearchRepository;
    }

    /**
     * Save a member.
     *
     * @param member the entity to save.
     * @return the persisted entity.
     */
    public Member save(Member member) {
        LOG.debug("Request to save Member : {}", member);
        member = memberRepository.save(member);
        memberSearchRepository.index(member);
        return member;
    }

    /**
     * Update a member.
     *
     * @param member the entity to save.
     * @return the persisted entity.
     */
    public Member update(Member member) {
        LOG.debug("Request to update Member : {}", member);
        member = memberRepository.save(member);
        memberSearchRepository.index(member);
        return member;
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
                if (member.getName() != null) {
                    existingMember.setName(member.getName());
                }
                if (member.getPhoto() != null) {
                    existingMember.setPhoto(member.getPhoto());
                }
                if (member.getPhotoContentType() != null) {
                    existingMember.setPhotoContentType(member.getPhotoContentType());
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
                if (member.getCity() != null) {
                    existingMember.setCity(member.getCity());
                }
                if (member.getState() != null) {
                    existingMember.setState(member.getState());
                }
                if (member.getZipCode() != null) {
                    existingMember.setZipCode(member.getZipCode());
                }
                if (member.getCityOfBirth() != null) {
                    existingMember.setCityOfBirth(member.getCityOfBirth());
                }
                if (member.getPreviousReligion() != null) {
                    existingMember.setPreviousReligion(member.getPreviousReligion());
                }
                if (member.getMaritalStatus() != null) {
                    existingMember.setMaritalStatus(member.getMaritalStatus());
                }
                if (member.getSpouseName() != null) {
                    existingMember.setSpouseName(member.getSpouseName());
                }
                if (member.getDateOfMarriage() != null) {
                    existingMember.setDateOfMarriage(member.getDateOfMarriage());
                }
                if (member.getStatus() != null) {
                    existingMember.setStatus(member.getStatus());
                }
                if (member.getCpf() != null) {
                    existingMember.setCpf(member.getCpf());
                }
                if (member.getRg() != null) {
                    existingMember.setRg(member.getRg());
                }
                if (member.getDateOfBaptism() != null) {
                    existingMember.setDateOfBaptism(member.getDateOfBaptism());
                }
                if (member.getChurchOfBaptism() != null) {
                    existingMember.setChurchOfBaptism(member.getChurchOfBaptism());
                }
                if (member.getDateOfMembership() != null) {
                    existingMember.setDateOfMembership(member.getDateOfMembership());
                }
                if (member.getTypeOfMembership() != null) {
                    existingMember.setTypeOfMembership(member.getTypeOfMembership());
                }
                if (member.getAssociationMeetingMinutes() != null) {
                    existingMember.setAssociationMeetingMinutes(member.getAssociationMeetingMinutes());
                }
                if (member.getDateOfDeath() != null) {
                    existingMember.setDateOfDeath(member.getDateOfDeath());
                }
                if (member.getDateOfExit() != null) {
                    existingMember.setDateOfExit(member.getDateOfExit());
                }
                if (member.getExitDestination() != null) {
                    existingMember.setExitDestination(member.getExitDestination());
                }
                if (member.getExitReason() != null) {
                    existingMember.setExitReason(member.getExitReason());
                }
                if (member.getExitMeetingMinutes() != null) {
                    existingMember.setExitMeetingMinutes(member.getExitMeetingMinutes());
                }
                if (member.getNotes() != null) {
                    existingMember.setNotes(member.getNotes());
                }

                return existingMember;
            })
            .map(memberRepository::save)
            .map(savedMember -> {
                memberSearchRepository.index(savedMember);
                return savedMember;
            });
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
     * Get all the members with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Member> findAllWithEagerRelationships(Pageable pageable) {
        return memberRepository.findAllWithEagerRelationships(pageable);
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
        return memberRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the member by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Member : {}", id);
        memberRepository.deleteById(id);
        memberSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the member corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Member> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Members for query {}", query);
        return memberSearchRepository.search(query, pageable);
    }
}
