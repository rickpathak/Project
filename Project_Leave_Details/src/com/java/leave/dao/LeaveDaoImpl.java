package com.java.leave.dao;

import java.util.ArrayList;
import java.util.List;
import com.java.leave.model.LeaveDetails;

public class LeaveDaoImpl implements LeaveDao {

    static List<LeaveDetails> leaveList = new ArrayList<>();

    @Override
    public boolean addLeave(LeaveDetails leave) {
        leaveList.add(leave);
        return true;
    }

    @Override
    public List<LeaveDetails> showLeave() {
        return leaveList;
    }

    @Override
    public LeaveDetails searchLeave(int leaveId) {
        for (LeaveDetails l : leaveList) {
            if (l.getLeaveId() == leaveId) {
                return l;
            }
        }
        return null;
    }

    @Override
    public boolean deleteLeave(int leaveId) {
        LeaveDetails leave = searchLeave(leaveId);
        if (leave != null) {
            leaveList.remove(leave);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateLeave(LeaveDetails leave) {
        LeaveDetails old = searchLeave(leave.getLeaveId());
        if (old != null) {
            leaveList.remove(old);
            leaveList.add(leave);
            return true;
        }
        return false;
    }
}
