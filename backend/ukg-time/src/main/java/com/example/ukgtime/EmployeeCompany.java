package com.example.ukgtime;

public class EmployeeCompany {
    private long eId;
    private long companyId;

    public EmployeeCompany(long eId, long companyId) {
        this.eId = eId;
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return String.format("EmployeeCompany[eId: %d, companyId: %d]", eId, companyId);
    }

    public long geteId() {
        return eId;
    }

    public void seteId(long eId) {
        this.eId = eId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }
}
