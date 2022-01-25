package com.csicit.ace.webservice.pojo;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author yansiyang
 * @version V1.0
 * @date 2019/8/6 14:12
 */
@Data
public class Employee {
    /**
     * 员工编号
     */
    private String code;

    public String getCode() {
        return code;
    }

    @XmlElement(name = "CODE")
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 工作证号
     */
    private String originCode;

    public String getOriginCode() {
        return originCode;
    }

    @XmlElement(name = "ORIGINCODE")
    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    /**
     * 员工姓名
     */
    private String name;

    public String getName() {
        return name;
    }

    @XmlElement(name = "NAME")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 姓名全拼
     */
    private String pinyin;

    public String getPinyin() {
        return pinyin;
    }

    @XmlElement(name = "PINYIN")
    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    /**
     * 性别
     */
    private Integer sex;

    public Integer getSex() {
        return sex;
    }

    @XmlElement(name = "SEX")
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * 性别_名称
     */
    private String sexName;

    public String getSexName() {
        return sexName;
    }

    @XmlElement(name = "SEX_NAME")
    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    /**
     * 身份证号
     */
    private String idCardNo;

    public String getIdCardNo() {
        return idCardNo;
    }

    @XmlElement(name = "IDCARDNO")
    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    /**
     * 出生日期
     */
    private String birthDate;

    public String getBirthDate() {
        return birthDate;
    }

    @XmlElement(name = "BIRTHDATE")
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * 民族
     */
    private String nation;

    public String getNation() {
        return nation;
    }

    @XmlElement(name = "NATION")
    public void setNation(String nation) {
        this.nation = nation;
    }

    /**
     * 民族_名称
     */
    private String natioanName;

    public String getNatioanName() {
        return natioanName;
    }

    @XmlElement(name = "NATION_NAME")
    public void setNatioanName(String natioanName) {
        this.natioanName = natioanName;
    }

    /**
     * 政治面貌
     */
    private String politicalOutlook;

    public String getPoliticalOutlook() {
        return politicalOutlook;
    }

    @XmlElement(name = "POLITICALOUTLOOK")
    public void setPoliticalOutlook(String politicalOutlook) {
        this.politicalOutlook = politicalOutlook;
    }

    /**
     * 政治面貌_名称
     */
    private String politicalOutlookName;

    public String getPoliticalOutlookName() {
        return politicalOutlookName;
    }

    @XmlElement(name = "POLITICALOUTLOOK_NAME")
    public void setPoliticalOutlookName(String politicalOutlookName) {
        this.politicalOutlookName = politicalOutlookName;
    }

    /**
     * 在岗状态
     */
    private String state;

    public String getState() {
        return state;
    }

    @XmlElement(name = "STATE")
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 在岗状态_名称
     */
    private String stateName;

    public String getStateName() {
        return stateName;
    }

    @XmlElement(name = "STATE_NAME")
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    /**
     * 编制
     */
    private String organization;

    public String getOrganization() {
        return organization;
    }

    @XmlElement(name = "ORGANIZATION")
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    /**
     * 编制_名称
     */
    private String organizationName;

    public String getOrganizationName() {
        return organizationName;
    }

    @XmlElement(name = "ORGANIZATION_NAME")
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    /**
     * 部门
     */
    private String department;

    public String getDepartment() {
        return department;
    }

    @XmlElement(name = "DEPARTMENT")
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * 部门_组织名称
     */
    private String departmentDeptFullName;

    public String getDepartmentDeptFullName() {
        return departmentDeptFullName;
    }

    @XmlElement(name = "DEPARTMENT_DEPTFULLNAME")
    public void setDepartmentDeptFullName(String departmentDeptFullName) {
        this.departmentDeptFullName = departmentDeptFullName;
    }

    /**
     * 兼职部门
     */
    private String partyDepartment;

    public String getPartyDepartment() {
        return partyDepartment;
    }

    @XmlElement(name = "PARTYDEPARTMENT")
    public void setPartyDepartment(String partyDepartment) {
        this.partyDepartment = partyDepartment;
    }

    /**
     * 兼职部门_组织名称
     */
    private String partyDepartmentDeptFullName;

    public String getPartyDepartmentDeptFullName() {
        return partyDepartmentDeptFullName;
    }

    @XmlElement(name = "PARTYDEPARTMENT_DEPTFULLNAME")
    public void setPartyDepartmentDeptFullName(String partyDepartmentDeptFullName) {
        this.partyDepartmentDeptFullName = partyDepartmentDeptFullName;
    }

    /**
     * 岗位
     */
    private String post;

    public String getPost() {
        return post;
    }

    @XmlElement(name = "POST")
    public void setPost(String post) {
        this.post = post;
    }

    /**
     * 岗位_名称
     */
    private String postName;

    public String getPostName() {
        return postName;
    }

    @XmlElement(name = "POST_NAME")
    public void setPostName(String postName) {
        this.postName = postName;
    }

    /**
     * 职称
     */
    private String title;

    public String getTitle() {
        return title;
    }

    @XmlElement(name = "TITLE")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 职称_名称
     */
    private String titleName;

    public String getTitleName() {
        return titleName;
    }

    @XmlElement(name = "TITLE_NAME")
    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    /**
     * 技术等级
     */
    private String skillLevel;

    public String getSkillLevel() {
        return skillLevel;
    }

    @XmlElement(name = "SKILLLEVEL")
    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    /**
     * 技术等级_名称
     */
    private String skillLevelName;

    public String getSkillLevelName() {
        return skillLevelName;
    }

    @XmlElement(name = "SKILLLEVEL_NAME")
    public void setSkillLevelName(String skillLevelName) {
        this.skillLevelName = skillLevelName;
    }

    /**
     * 涉密等级
     */
    private String staffRank;


    public String getStaffRank() {
        return staffRank;
    }

    @XmlElement(name = "STAFFRANK")
    public void setStaffRank(String staffRank) {
        this.staffRank = staffRank;
    }

    /**
     * 涉密等级_名称
     */
    private String staffRankName;

    public String getStaffRankName() {
        return staffRankName;
    }

    @XmlElement(name = "STAFFRANK_NAME")
    public void setStaffRankName(String staffRankName) {
        this.staffRankName = staffRankName;
    }

    /**
     * 参加工作日期
     */
    private String startWorkDate;

    public String getStartWorkDate() {
        return startWorkDate;
    }

    @XmlElement(name = "STARTWORKDATE")
    public void setStartWorkDate(String startWorkDate) {
        this.startWorkDate = startWorkDate;
    }

    /**
     * 进入本单位日期
     */
    private String enterTheDate;

    public String getEnterTheDate() {
        return enterTheDate;
    }

    @XmlElement(name = "ENTERTHEDATE")
    public void setEnterTheDate(String enterTheDate) {
        this.enterTheDate = enterTheDate;
    }

    /**
     * 最高学历
     */
    private String highestEducation;


    public String getHighestEducation() {
        return highestEducation;
    }

    @XmlElement(name = "HIGHESTEDUCATION")
    public void setHighestEducation(String highestEducation) {
        this.highestEducation = highestEducation;
    }

    /**
     * 最高学历_名称
     */
    private String highestEducationName;

    public String getHighestEducationName() {
        return highestEducationName;
    }

    @XmlElement(name = "HIGHESTEDUCATION_NAME")
    public void setHighestEducationName(String highestEducationName) {
        this.highestEducationName = highestEducationName;
    }

    /**
     * 最高学位
     */
    private String highestDegree;

    public String getHighestDegree() {
        return highestDegree;
    }

    @XmlElement(name = "HIGHESTDEGREE")
    public void setHighestDegree(String highestDegree) {
        this.highestDegree = highestDegree;
    }

    /**
     * 最高学位_名称
     */
    private String highestDegreeName;

    public String getHighestDegreeName() {
        return highestDegreeName;
    }


    @XmlElement(name = "HIGHESTDEGREE_NAME")
    public void setHighestDegreeName(String highestDegreeName) {
        this.highestDegreeName = highestDegreeName;
    }

    /**
     * 最高学历所学专业
     */
    private String majorDegree;

    public String getMajorDegree() {
        return majorDegree;
    }

    @XmlElement(name = "MAJORDEGREE")
    public void setMajorDegree(String majorDegree) {
        this.majorDegree = majorDegree;
    }

    /**
     * 最高学历毕业院校
     */
    private String graduateSchool;

    public String getGraduateSchool() {
        return graduateSchool;
    }

    @XmlElement(name = "GRADUATESCHOOL")
    public void setGraduateSchool(String graduateSchool) {
        this.graduateSchool = graduateSchool;
    }

    /**
     * 移动电话
     */
    private String mobilePhone;

    public String getMobilePhone() {
        return mobilePhone;
    }

    @XmlElement(name = "MOBILEPHONE")
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    /**
     * 数据状态
     */
    private String dataStatus;

    public String getDataStatus() {
        return dataStatus;
    }

    @XmlElement(name = "DATASTATUS")
    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }
}
