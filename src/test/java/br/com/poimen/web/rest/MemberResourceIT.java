package br.com.poimen.web.rest;

import static br.com.poimen.domain.MemberAsserts.*;
import static br.com.poimen.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.poimen.IntegrationTest;
import br.com.poimen.domain.Member;
import br.com.poimen.domain.enumeration.ExitReason;
import br.com.poimen.domain.enumeration.MaritalStatus;
import br.com.poimen.domain.enumeration.MemberStatus;
import br.com.poimen.domain.enumeration.MembershipType;
import br.com.poimen.repository.MemberRepository;
import br.com.poimen.repository.search.MemberSearchRepository;
import br.com.poimen.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MemberResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MemberResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY_OF_BIRTH = "AAAAAAAAAA";
    private static final String UPDATED_CITY_OF_BIRTH = "BBBBBBBBBB";

    private static final String DEFAULT_PREVIOUS_RELIGION = "AAAAAAAAAA";
    private static final String UPDATED_PREVIOUS_RELIGION = "BBBBBBBBBB";

    private static final MaritalStatus DEFAULT_MARITAL_STATUS = MaritalStatus.SINGLE;
    private static final MaritalStatus UPDATED_MARITAL_STATUS = MaritalStatus.MARRIED;

    private static final String DEFAULT_SPOUSE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SPOUSE_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_MARRIAGE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_MARRIAGE = LocalDate.now(ZoneId.systemDefault());

    private static final MemberStatus DEFAULT_STATUS = MemberStatus.COMUNGANT_MEMBER;
    private static final MemberStatus UPDATED_STATUS = MemberStatus.NON_COMUNGANT_MEMBER;

    private static final String DEFAULT_CPF = "02309291487";
    private static final String UPDATED_CPF = "63871813709";

    private static final String DEFAULT_RG = "AAAAAAAAAA";
    private static final String UPDATED_RG = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BAPTISM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BAPTISM = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CHURCH_OF_BAPTISM = "AAAAAAAAAA";
    private static final String UPDATED_CHURCH_OF_BAPTISM = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_MEMBERSHIP = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_MEMBERSHIP = LocalDate.now(ZoneId.systemDefault());

    private static final MembershipType DEFAULT_TYPE_OF_MEMBERSHIP = MembershipType.PROFESSION_OF_FAITH;
    private static final MembershipType UPDATED_TYPE_OF_MEMBERSHIP = MembershipType.TRANSFER;

    private static final String DEFAULT_ASSOCIATION_MEETING_MINUTES = "AAAAAAAAAA";
    private static final String UPDATED_ASSOCIATION_MEETING_MINUTES = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_DEATH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_DEATH = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_OF_EXIT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_EXIT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_EXIT_DESTINATION = "AAAAAAAAAA";
    private static final String UPDATED_EXIT_DESTINATION = "BBBBBBBBBB";

    private static final ExitReason DEFAULT_EXIT_REASON = ExitReason.TRANSFER;
    private static final ExitReason UPDATED_EXIT_REASON = ExitReason.EXCOMMUNICATION;

    private static final String DEFAULT_EXIT_MEETING_MINUTES = "AAAAAAAAAA";
    private static final String UPDATED_EXIT_MEETING_MINUTES = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/members/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MemberRepository memberRepository;

    @Mock
    private MemberRepository memberRepositoryMock;

    @Mock
    private MemberService memberServiceMock;

    @Autowired
    private MemberSearchRepository memberSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMemberMockMvc;

    private Member member;

    private Member insertedMember;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Member createEntity() {
        return new Member()
            .name(DEFAULT_NAME)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .zipCode(DEFAULT_ZIP_CODE)
            .cityOfBirth(DEFAULT_CITY_OF_BIRTH)
            .previousReligion(DEFAULT_PREVIOUS_RELIGION)
            .maritalStatus(DEFAULT_MARITAL_STATUS)
            .spouseName(DEFAULT_SPOUSE_NAME)
            .dateOfMarriage(DEFAULT_DATE_OF_MARRIAGE)
            .status(DEFAULT_STATUS)
            .cpf(DEFAULT_CPF)
            .rg(DEFAULT_RG)
            .dateOfBaptism(DEFAULT_DATE_OF_BAPTISM)
            .churchOfBaptism(DEFAULT_CHURCH_OF_BAPTISM)
            .dateOfMembership(DEFAULT_DATE_OF_MEMBERSHIP)
            .typeOfMembership(DEFAULT_TYPE_OF_MEMBERSHIP)
            .associationMeetingMinutes(DEFAULT_ASSOCIATION_MEETING_MINUTES)
            .dateOfDeath(DEFAULT_DATE_OF_DEATH)
            .dateOfExit(DEFAULT_DATE_OF_EXIT)
            .exitDestination(DEFAULT_EXIT_DESTINATION)
            .exitReason(DEFAULT_EXIT_REASON)
            .exitMeetingMinutes(DEFAULT_EXIT_MEETING_MINUTES)
            .notes(DEFAULT_NOTES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Member createUpdatedEntity() {
        return new Member()
            .name(UPDATED_NAME)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .cityOfBirth(UPDATED_CITY_OF_BIRTH)
            .previousReligion(UPDATED_PREVIOUS_RELIGION)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .spouseName(UPDATED_SPOUSE_NAME)
            .dateOfMarriage(UPDATED_DATE_OF_MARRIAGE)
            .status(UPDATED_STATUS)
            .cpf(UPDATED_CPF)
            .rg(UPDATED_RG)
            .dateOfBaptism(UPDATED_DATE_OF_BAPTISM)
            .churchOfBaptism(UPDATED_CHURCH_OF_BAPTISM)
            .dateOfMembership(UPDATED_DATE_OF_MEMBERSHIP)
            .typeOfMembership(UPDATED_TYPE_OF_MEMBERSHIP)
            .associationMeetingMinutes(UPDATED_ASSOCIATION_MEETING_MINUTES)
            .dateOfDeath(UPDATED_DATE_OF_DEATH)
            .dateOfExit(UPDATED_DATE_OF_EXIT)
            .exitDestination(UPDATED_EXIT_DESTINATION)
            .exitReason(UPDATED_EXIT_REASON)
            .exitMeetingMinutes(UPDATED_EXIT_MEETING_MINUTES)
            .notes(UPDATED_NOTES);
    }

    @BeforeEach
    public void initTest() {
        member = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMember != null) {
            memberRepository.delete(insertedMember);
            memberSearchRepository.delete(insertedMember);
            insertedMember = null;
        }
    }

    @Test
    @Transactional
    void createMember() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(memberSearchRepository.findAll());
        // Create the Member
        var returnedMember = om.readValue(
            restMemberMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(member)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Member.class
        );

        // Validate the Member in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMemberUpdatableFieldsEquals(returnedMember, getPersistedMember(returnedMember));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(memberSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedMember = returnedMember;
    }

    @Test
    @Transactional
    void createMemberWithExistingId() throws Exception {
        // Create the Member with an existing ID
        member.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(memberSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(member)))
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(memberSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(memberSearchRepository.findAll());
        // set the field null
        member.setName(null);

        // Create the Member, which fails.

        restMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(member)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(memberSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDateOfBirthIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(memberSearchRepository.findAll());
        // set the field null
        member.setDateOfBirth(null);

        // Create the Member, which fails.

        restMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(member)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(memberSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkMaritalStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(memberSearchRepository.findAll());
        // set the field null
        member.setMaritalStatus(null);

        // Create the Member, which fails.

        restMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(member)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(memberSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(memberSearchRepository.findAll());
        // set the field null
        member.setStatus(null);

        // Create the Member, which fails.

        restMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(member)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(memberSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCpfIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(memberSearchRepository.findAll());
        // set the field null
        member.setCpf(null);

        // Create the Member, which fails.

        restMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(member)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(memberSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkRgIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(memberSearchRepository.findAll());
        // set the field null
        member.setRg(null);

        // Create the Member, which fails.

        restMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(member)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(memberSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllMembers() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList
        restMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].cityOfBirth").value(hasItem(DEFAULT_CITY_OF_BIRTH)))
            .andExpect(jsonPath("$.[*].previousReligion").value(hasItem(DEFAULT_PREVIOUS_RELIGION)))
            .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].spouseName").value(hasItem(DEFAULT_SPOUSE_NAME)))
            .andExpect(jsonPath("$.[*].dateOfMarriage").value(hasItem(DEFAULT_DATE_OF_MARRIAGE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].rg").value(hasItem(DEFAULT_RG)))
            .andExpect(jsonPath("$.[*].dateOfBaptism").value(hasItem(DEFAULT_DATE_OF_BAPTISM.toString())))
            .andExpect(jsonPath("$.[*].churchOfBaptism").value(hasItem(DEFAULT_CHURCH_OF_BAPTISM)))
            .andExpect(jsonPath("$.[*].dateOfMembership").value(hasItem(DEFAULT_DATE_OF_MEMBERSHIP.toString())))
            .andExpect(jsonPath("$.[*].typeOfMembership").value(hasItem(DEFAULT_TYPE_OF_MEMBERSHIP.toString())))
            .andExpect(jsonPath("$.[*].associationMeetingMinutes").value(hasItem(DEFAULT_ASSOCIATION_MEETING_MINUTES)))
            .andExpect(jsonPath("$.[*].dateOfDeath").value(hasItem(DEFAULT_DATE_OF_DEATH.toString())))
            .andExpect(jsonPath("$.[*].dateOfExit").value(hasItem(DEFAULT_DATE_OF_EXIT.toString())))
            .andExpect(jsonPath("$.[*].exitDestination").value(hasItem(DEFAULT_EXIT_DESTINATION)))
            .andExpect(jsonPath("$.[*].exitReason").value(hasItem(DEFAULT_EXIT_REASON.toString())))
            .andExpect(jsonPath("$.[*].exitMeetingMinutes").value(hasItem(DEFAULT_EXIT_MEETING_MINUTES)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMembersWithEagerRelationshipsIsEnabled() throws Exception {
        when(memberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMemberMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(memberServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMembersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(memberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMemberMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(memberRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMember() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get the member
        restMemberMockMvc
            .perform(get(ENTITY_API_URL_ID, member.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(member.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64.getEncoder().encodeToString(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE))
            .andExpect(jsonPath("$.cityOfBirth").value(DEFAULT_CITY_OF_BIRTH))
            .andExpect(jsonPath("$.previousReligion").value(DEFAULT_PREVIOUS_RELIGION))
            .andExpect(jsonPath("$.maritalStatus").value(DEFAULT_MARITAL_STATUS.toString()))
            .andExpect(jsonPath("$.spouseName").value(DEFAULT_SPOUSE_NAME))
            .andExpect(jsonPath("$.dateOfMarriage").value(DEFAULT_DATE_OF_MARRIAGE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF))
            .andExpect(jsonPath("$.rg").value(DEFAULT_RG))
            .andExpect(jsonPath("$.dateOfBaptism").value(DEFAULT_DATE_OF_BAPTISM.toString()))
            .andExpect(jsonPath("$.churchOfBaptism").value(DEFAULT_CHURCH_OF_BAPTISM))
            .andExpect(jsonPath("$.dateOfMembership").value(DEFAULT_DATE_OF_MEMBERSHIP.toString()))
            .andExpect(jsonPath("$.typeOfMembership").value(DEFAULT_TYPE_OF_MEMBERSHIP.toString()))
            .andExpect(jsonPath("$.associationMeetingMinutes").value(DEFAULT_ASSOCIATION_MEETING_MINUTES))
            .andExpect(jsonPath("$.dateOfDeath").value(DEFAULT_DATE_OF_DEATH.toString()))
            .andExpect(jsonPath("$.dateOfExit").value(DEFAULT_DATE_OF_EXIT.toString()))
            .andExpect(jsonPath("$.exitDestination").value(DEFAULT_EXIT_DESTINATION))
            .andExpect(jsonPath("$.exitReason").value(DEFAULT_EXIT_REASON.toString()))
            .andExpect(jsonPath("$.exitMeetingMinutes").value(DEFAULT_EXIT_MEETING_MINUTES))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMember() throws Exception {
        // Get the member
        restMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMember() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        memberSearchRepository.save(member);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(memberSearchRepository.findAll());

        // Update the member
        Member updatedMember = memberRepository.findById(member.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMember are not directly saved in db
        em.detach(updatedMember);
        updatedMember
            .name(UPDATED_NAME)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .cityOfBirth(UPDATED_CITY_OF_BIRTH)
            .previousReligion(UPDATED_PREVIOUS_RELIGION)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .spouseName(UPDATED_SPOUSE_NAME)
            .dateOfMarriage(UPDATED_DATE_OF_MARRIAGE)
            .status(UPDATED_STATUS)
            .cpf(UPDATED_CPF)
            .rg(UPDATED_RG)
            .dateOfBaptism(UPDATED_DATE_OF_BAPTISM)
            .churchOfBaptism(UPDATED_CHURCH_OF_BAPTISM)
            .dateOfMembership(UPDATED_DATE_OF_MEMBERSHIP)
            .typeOfMembership(UPDATED_TYPE_OF_MEMBERSHIP)
            .associationMeetingMinutes(UPDATED_ASSOCIATION_MEETING_MINUTES)
            .dateOfDeath(UPDATED_DATE_OF_DEATH)
            .dateOfExit(UPDATED_DATE_OF_EXIT)
            .exitDestination(UPDATED_EXIT_DESTINATION)
            .exitReason(UPDATED_EXIT_REASON)
            .exitMeetingMinutes(UPDATED_EXIT_MEETING_MINUTES)
            .notes(UPDATED_NOTES);

        restMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMember.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMember))
            )
            .andExpect(status().isOk());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMemberToMatchAllProperties(updatedMember);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(memberSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Member> memberSearchList = Streamable.of(memberSearchRepository.findAll()).toList();
                Member testMemberSearch = memberSearchList.get(searchDatabaseSizeAfter - 1);

                assertMemberAllPropertiesEquals(testMemberSearch, updatedMember);
            });
    }

    @Test
    @Transactional
    void putNonExistingMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(memberSearchRepository.findAll());
        member.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(put(ENTITY_API_URL_ID, member.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(member)))
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(memberSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(memberSearchRepository.findAll());
        member.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(member))
            )
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(memberSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(memberSearchRepository.findAll());
        member.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(member)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(memberSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateMemberWithPatch() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the member using partial update
        Member partialUpdatedMember = new Member();
        partialUpdatedMember.setId(member.getId());

        partialUpdatedMember
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .address(UPDATED_ADDRESS)
            .cityOfBirth(UPDATED_CITY_OF_BIRTH)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .dateOfMarriage(UPDATED_DATE_OF_MARRIAGE)
            .cpf(UPDATED_CPF)
            .rg(UPDATED_RG)
            .churchOfBaptism(UPDATED_CHURCH_OF_BAPTISM)
            .dateOfMembership(UPDATED_DATE_OF_MEMBERSHIP)
            .typeOfMembership(UPDATED_TYPE_OF_MEMBERSHIP)
            .dateOfDeath(UPDATED_DATE_OF_DEATH)
            .exitDestination(UPDATED_EXIT_DESTINATION)
            .notes(UPDATED_NOTES);

        restMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMember))
            )
            .andExpect(status().isOk());

        // Validate the Member in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMemberUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMember, member), getPersistedMember(member));
    }

    @Test
    @Transactional
    void fullUpdateMemberWithPatch() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the member using partial update
        Member partialUpdatedMember = new Member();
        partialUpdatedMember.setId(member.getId());

        partialUpdatedMember
            .name(UPDATED_NAME)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .cityOfBirth(UPDATED_CITY_OF_BIRTH)
            .previousReligion(UPDATED_PREVIOUS_RELIGION)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .spouseName(UPDATED_SPOUSE_NAME)
            .dateOfMarriage(UPDATED_DATE_OF_MARRIAGE)
            .status(UPDATED_STATUS)
            .cpf(UPDATED_CPF)
            .rg(UPDATED_RG)
            .dateOfBaptism(UPDATED_DATE_OF_BAPTISM)
            .churchOfBaptism(UPDATED_CHURCH_OF_BAPTISM)
            .dateOfMembership(UPDATED_DATE_OF_MEMBERSHIP)
            .typeOfMembership(UPDATED_TYPE_OF_MEMBERSHIP)
            .associationMeetingMinutes(UPDATED_ASSOCIATION_MEETING_MINUTES)
            .dateOfDeath(UPDATED_DATE_OF_DEATH)
            .dateOfExit(UPDATED_DATE_OF_EXIT)
            .exitDestination(UPDATED_EXIT_DESTINATION)
            .exitReason(UPDATED_EXIT_REASON)
            .exitMeetingMinutes(UPDATED_EXIT_MEETING_MINUTES)
            .notes(UPDATED_NOTES);

        restMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMember))
            )
            .andExpect(status().isOk());

        // Validate the Member in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMemberUpdatableFieldsEquals(partialUpdatedMember, getPersistedMember(partialUpdatedMember));
    }

    @Test
    @Transactional
    void patchNonExistingMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(memberSearchRepository.findAll());
        member.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, member.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(member))
            )
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(memberSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(memberSearchRepository.findAll());
        member.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(member))
            )
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(memberSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(memberSearchRepository.findAll());
        member.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(member)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(memberSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteMember() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);
        memberRepository.save(member);
        memberSearchRepository.save(member);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(memberSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the member
        restMemberMockMvc
            .perform(delete(ENTITY_API_URL_ID, member.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(memberSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchMember() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);
        memberSearchRepository.save(member);

        // Search the member
        restMemberMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + member.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].cityOfBirth").value(hasItem(DEFAULT_CITY_OF_BIRTH)))
            .andExpect(jsonPath("$.[*].previousReligion").value(hasItem(DEFAULT_PREVIOUS_RELIGION)))
            .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].spouseName").value(hasItem(DEFAULT_SPOUSE_NAME)))
            .andExpect(jsonPath("$.[*].dateOfMarriage").value(hasItem(DEFAULT_DATE_OF_MARRIAGE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].rg").value(hasItem(DEFAULT_RG)))
            .andExpect(jsonPath("$.[*].dateOfBaptism").value(hasItem(DEFAULT_DATE_OF_BAPTISM.toString())))
            .andExpect(jsonPath("$.[*].churchOfBaptism").value(hasItem(DEFAULT_CHURCH_OF_BAPTISM)))
            .andExpect(jsonPath("$.[*].dateOfMembership").value(hasItem(DEFAULT_DATE_OF_MEMBERSHIP.toString())))
            .andExpect(jsonPath("$.[*].typeOfMembership").value(hasItem(DEFAULT_TYPE_OF_MEMBERSHIP.toString())))
            .andExpect(jsonPath("$.[*].associationMeetingMinutes").value(hasItem(DEFAULT_ASSOCIATION_MEETING_MINUTES)))
            .andExpect(jsonPath("$.[*].dateOfDeath").value(hasItem(DEFAULT_DATE_OF_DEATH.toString())))
            .andExpect(jsonPath("$.[*].dateOfExit").value(hasItem(DEFAULT_DATE_OF_EXIT.toString())))
            .andExpect(jsonPath("$.[*].exitDestination").value(hasItem(DEFAULT_EXIT_DESTINATION)))
            .andExpect(jsonPath("$.[*].exitReason").value(hasItem(DEFAULT_EXIT_REASON.toString())))
            .andExpect(jsonPath("$.[*].exitMeetingMinutes").value(hasItem(DEFAULT_EXIT_MEETING_MINUTES)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    protected long getRepositoryCount() {
        return memberRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Member getPersistedMember(Member member) {
        return memberRepository.findById(member.getId()).orElseThrow();
    }

    protected void assertPersistedMemberToMatchAllProperties(Member expectedMember) {
        assertMemberAllPropertiesEquals(expectedMember, getPersistedMember(expectedMember));
    }

    protected void assertPersistedMemberToMatchUpdatableProperties(Member expectedMember) {
        assertMemberAllUpdatablePropertiesEquals(expectedMember, getPersistedMember(expectedMember));
    }
}
