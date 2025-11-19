package com.java.leave.dao;

import java.util.List;
import com.java.leave.model.LeaveDetails;

public interface LeaveDao {

    boolean addLeave(LeaveDetails leave);

    List<LeaveDetails> showLeave();

    LeaveDetails searchLeave(int leaveId);

    boolean deleteLeave(int leaveId);

    boolean updateLeave(LeaveDetails leave);
}
