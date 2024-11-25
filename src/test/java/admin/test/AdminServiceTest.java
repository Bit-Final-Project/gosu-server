package admin.test;

import admin.repository.AdminMemberRepository;
import admin.service.impl.AdminServiceImpl;
import member.bean.MemberStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class AdminServiceTest {

    @Mock
    private AdminMemberRepository adminMemberRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mock 객체 초기화
    }

    @Test
    public void testGetUserCountForUser() {

        // given
        when(adminMemberRepository.countByMemberStatus(MemberStatus.USER)).thenReturn(100L);

        // when
        long userCount = adminService.getUserCount(MemberStatus.USER);

        // then
        assertThat(userCount).isEqualTo(100L);
    }

    @Test
    public void testGetUserCountForMaster() {

        // given
        when(adminMemberRepository.countByMemberStatus(MemberStatus.PRO)).thenReturn(50L);

        // when
        long masterCount = adminService.getUserCount(MemberStatus.PRO);

        // then
        assertThat(masterCount).isEqualTo(50L);
    }

    @Test
    public void testGetUserCountForDeactivated() {

        // given
        when(adminMemberRepository.countByMemberStatus(MemberStatus.CANCEL)).thenReturn(10L);

        // when
        long deactivatedCount = adminService.getUserCount(MemberStatus.CANCEL);

        // then
        assertThat(deactivatedCount).isEqualTo(10L);
    }


}
