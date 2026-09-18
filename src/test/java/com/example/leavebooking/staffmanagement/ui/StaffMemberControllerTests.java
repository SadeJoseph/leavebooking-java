package com.example.leavebooking.staffmanagement.ui;

import com.example.leavebooking.staffmanagement.ContextFacade;
import com.example.leavebooking.staffmanagement.application.dto.StaffMemberDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StaffMemberController.class)
@DisplayName("StaffMemberController MVC Unit Tests")
class StaffMemberControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ContextFacade facade;

  private StaffMemberDTO createValidStaffMemberDTO(
      String id) {

    return new StaffMemberDTO(
        id,
        "Sade",
        "Joseph",
        "sade.joseph@example.com");
  }

  @Nested
  @DisplayName("Find All Staff Members")
  class FindAllStaffMembers {

    @Test
    @DisplayName("All staff members are returned with HTTP 200")
    void test01() throws Exception {

      // Arrange
      StaffMemberDTO staffMember = createValidStaffMemberDTO("0001");

      given(facade.findAllStaffMembers())
          .willReturn(List.of(staffMember));

      // Act + Assert
      mockMvc.perform(
          get("/staff")
          .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.length()").value(1))
          .andExpect(jsonPath("$[0].id").value("0001"))
          .andExpect(jsonPath("$[0].firstName").value("Sade"))
          .andExpect(jsonPath("$[0].surname").value("Joseph"))
          .andExpect(
              jsonPath("$[0].emailAddress")
                  .value("sade.joseph@example.com"));
    }
  }

  @Nested
  @DisplayName("Find Staff Member By Id")
  class FindStaffMemberById {

    @Test
    @DisplayName("A staff member is returned with HTTP 200 when the id exists")
    void test01() throws Exception {

      // Arrange
      StaffMemberDTO staffMember = createValidStaffMemberDTO("0001");

      given(facade.findStaffMemberById("0001"))
          .willReturn(staffMember);

      // Act + Assert
      mockMvc.perform(
          get("/staff/{staff_id}", "0001")
              .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(
              content().contentType(
                  MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.id").value("0001"))
          .andExpect(jsonPath("$.firstName").value("Sade"))
          .andExpect(jsonPath("$.surname").value("Joseph"));
    }
  }
}