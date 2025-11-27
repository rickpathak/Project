package com.java.leave.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.java.leave.bal.LeaveBal;
import com.java.leave.exception.LeaveException;
import com.java.leave.model.LeaveDetails;

public class LeaveMain {

    static LeaveBal bal = new LeaveBal();
    static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n==== LEAVE MANAGEMENT ====");
            System.out.println("1. Add Leave");
            System.out.println("2. Show Leave");
            System.out.println("3. Search Leave");
            System.out.println("4. Update Leave");
            System.out.println("5. Delete Leave");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();

            switch (ch) {

                case 1:
                    try {
                        LeaveDetails leave = readLeave(sc);
                        if (bal.addLeaveBal(leave))
                            System.out.println("Leave Added Successfully!");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    List<LeaveDetails> list = bal.showLeaveBal();
                    for (LeaveDetails l : list) {
                        printLeave(l);
                    }
                    break;

                case 3:
                    System.out.print("Enter LeaveId: ");
                    int lid = sc.nextInt();
                    LeaveDetails l = bal.searchLeaveBal(lid);
                    if (l != null)
                        printLeave(l);
                    else
                        System.out.println("Record Not Found!");
                    break;

                case 4:
                    try {
                        LeaveDetails leave = readLeave(sc);
                        if (bal.updateLeaveBal(leave))
                            System.out.println("Leave Updated Successfully!");
                        else
                            System.out.println("Record Not Found!");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 5:
                    System.out.print("Enter LeaveId: ");
                    int delId = sc.nextInt();
                    if (bal.deleteLeaveBal(delId))
                        System.out.println("Leave Deleted!");
                    else
                        System.out.println("Record Not Found!");
                    break;

                case 6:
                    System.out.println("Thank you!");
                    System.exit(0);
            }
        }
    }

    private static LeaveDetails readLeave(Scanner sc) throws Exception {

        LeaveDetails leave = new LeaveDetails();

        System.out.print("Enter LeaveId: ");
        leave.setLeaveId(sc.nextInt());

        System.out.print("Enter EmpId: ");
        leave.setEmpId(sc.nextInt());

        System.out.print("Enter Start Date (dd-MM-yyyy): ");
        leave.setLeaveStartDate(sdf.parse(sc.next()));

        System.out.print("Enter End Date (dd-MM-yyyy): ");
        leave.setLeaveEndDate(sdf.parse(sc.next()));

        System.out.print("Enter Reason: ");
        leave.setLeaveReason(sc.next());

        return leave;
    }

    private static void printLeave(LeaveDetails l) {
        System.out.println(
                "LeaveId: " + l.getLeaveId() +
                        " | EmpId: " + l.getEmpId() +
                        " | Start: " + sdf.format(l.getLeaveStartDate()) +
                        " | End: " + sdf.format(l.getLeaveEndDate()) +
                        " | Days: " + l.getNoOfDays() +
                        " | AppliedOn: " + sdf.format(l.getAppliedOn()) +
                        " | Reason: " + l.getLeaveReason());
    }
}
