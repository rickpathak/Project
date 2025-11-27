package com.java.leave.bal;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.java.leave.dao.LeaveDao;
import com.java.leave.dao.LeaveDaoImpl;
import com.java.leave.exception.LeaveException;
import com.java.leave.model.LeaveDetails;

public class LeaveBal {

    private static LeaveDao dao = new LeaveDaoImpl();

    public boolean addLeaveBal(LeaveDetails leave) throws LeaveException {
        validate(leave);
        return dao.addLeave(leave);
    }

    public boolean updateLeaveBal(LeaveDetails leave) throws LeaveException {
        validate(leave);
        return dao.updateLeave(leave);
    }

    public LeaveDetails searchLeaveBal(int leaveId) {
        return dao.searchLeave(leaveId);
    }

    public boolean deleteLeaveBal(int leaveId) {
        return dao.deleteLeave(leaveId);
    }

    public java.util.List<LeaveDetails> showLeaveBal() {
        return dao.showLeave();
    }

    private void validate(LeaveDetails leave) throws LeaveException {

        Date start = leave.getLeaveStartDate();
        Date end = leave.getLeaveEndDate();
        Date today = new Date();

        if (start.before(today))
            throw new LeaveException("Start date cannot be yesterday");

        if (end.before(today))
            throw new LeaveException("End date cannot be yesterday");

        if (start.after(end))
            throw new LeaveException("Start date cannot be greater than end date");

        long diff = end.getTime() - start.getTime();
        int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;

        leave.setNoOfDays(days);
        leave.setAppliedOn(new Date());
    }
}
