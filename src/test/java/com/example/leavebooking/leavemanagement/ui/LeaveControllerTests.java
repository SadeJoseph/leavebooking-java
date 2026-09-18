package com.example.leavebooking.leavemanagement.ui;

import com.example.leavebooking.leavemanagement.ContextFacade;
import com.example.leavebooking.leavemanagement.application.dto.LeaveRequestDTO;
import com.example.leavebooking.leavemanagement.domain.DateRange;
import com.example.leavebooking.leavemanagement.ui.commands.AddLeaveRequestCommand;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LeaveController.class)
@DisplayName("LeaveController MVC Unit Tests")
class LeaveControllerTests {

  @Autowired
  private MockMvc mockMvc;

 private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  @MockitoBean
  private ContextFacade facade;

  private LeaveRequestDTO createValidLeaveRequestDTO() {

    return new LeaveRequestDTO(
        "1001",
        "0001",
        LocalDate.of(2026, 11, 2),
        LocalDate.of(2026, 11, 6),
        "Annual leave",
        "ANNUAL_LEAVE",
        "PENDING",
        "Leave request pending");
  }

  @Nested
  @DisplayName("Find All Leave Requests")
  class FindAllLeaveRequests {

    @Test
    @DisplayName("All leave requests are returned with HTTP 200")
    void test01() throws Exception {

      // Arrange
      LeaveRequestDTO leaveRequest = createValidLeaveRequestDTO();

      given(facade.findAllLeaveRequests())
          .willReturn(List.of(leaveRequest));

      // Act + Assert
      mockMvc.perform(
          get("/leave/requests")
              .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.length()").value(1))
          .andExpect(jsonPath("$[0].id").value("1001"))
          .andExpect(jsonPath("$[0].staffId").value("0001"));
    }
  }

  @Nested
  @DisplayName("Find Leave Requests By Staff Id")
  class FindLeaveRequestsByStaffId {

    @Test
    @DisplayName("Leave requests for a staff member are returned with HTTP 200")
    void test01() throws Exception {

      // Arrange
      LeaveRequestDTO leaveRequest = createValidLeaveRequestDTO();

      given(facade.findLeaveRequestsByStaffId("0001"))
          .willReturn(List.of(leaveRequest));

      // Act + Assert
      mockMvc.perform(
          get("/leave/requests/staff/{staff_id}", "0001")
              .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.length()").value(1))
          .andExpect(jsonPath("$[0].staffId").value("0001"));
    }
  }

  @Nested
  @DisplayName("Add Leave Request")
  class AddLeaveRequest {

    @Test
    @DisplayName("A valid leave request returns HTTP 201")
    void test01() throws Exception {

      // Arrange
      AddLeaveRequestCommand command = new AddLeaveRequestCommand(
          "0001",
          new DateRange(
              LocalDate.of(2026, 11, 2),
              LocalDate.of(2026, 11, 6)),
          "Annual leave");

      // Act + Assert
      mockMvc.perform(
          post("/leave/requests")
              .contentType(MediaType.APPLICATION_JSON)
              .content(
                  objectMapper.writeValueAsString(
                      command)))
          .andExpect(status().isCreated())
          .andExpect(content().string(""));

      verify(facade)
          .addLeaveRequest(
              any(AddLeaveRequestCommand.class));
    }
  }
}