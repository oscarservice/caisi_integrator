package org.oscarehr.caisi_integrator.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.persistence.jdbc.Index;

@Entity
public class CachedBillingOnItem extends AbstractModel<FacilityIdIntegerCompositePk> implements Comparable<CachedBillingOnItem>
{
	@EmbeddedId
	private FacilityIdIntegerCompositePk facilityBillingOnItemPk;

        @Column(nullable=false)
        @Index
        private Integer caisiDemographicId = null;

        @Column(nullable=false, length=16)
        @Index
        private String caisiProviderId = null;

        @Column(length=16)
        private String apptProviderId = null;

        @Column(length=16)
        private String asstProviderId = null;

        @Column
        private Integer appointmentId = null;

        @Temporal(TemporalType.DATE)
        private Date serviceDate = null;

        @Column(length=20)
        private String serviceCode = null;

        @Column(length=4)
        private String dx = null;

        @Column(length=4)
        private String dx1 = null;

        @Column(length=4)
        private String dx2 = null;

        @Column(length=1)
        private String status = null;


	public FacilityIdIntegerCompositePk getFacilityIdIntegerCompositePk()
	{
		return facilityBillingOnItemPk;
	}

	public void setFacilityIdIntegerCompositePk(FacilityIdIntegerCompositePk facilityBillingOnItemPk)
	{
		this.facilityBillingOnItemPk = facilityBillingOnItemPk;
	}

        public Integer getCaisiDemographicId()
        {
                return this.caisiDemographicId;
        }

        public void setCaisiDemographicId(Integer caisiDemographicId)
        {
                this.caisiDemographicId = caisiDemographicId;
        }

        public String getCaisiProviderId()
        {
                return this.caisiProviderId;
        }

        public void setCaisiProviderId(String caisiProviderId)
        {
                this.caisiProviderId = StringUtils.trimToEmpty(caisiProviderId);
        }

        public String getApptProviderId()
        {
                return this.apptProviderId;
        }

        public void setApptProviderId(String apptProviderId)
        {
                this.apptProviderId = StringUtils.trimToNull(apptProviderId);
        }

        public String getAsstProviderId()
        {
                return this.asstProviderId;
        }

        public void setAsstProviderId(String asstProviderId)
        {
                this.asstProviderId = StringUtils.trimToNull(asstProviderId);
        }

        public Integer getAppointmentId()
        {
                return this.appointmentId;
        }

        public void setAppointmentId(Integer appointmentId)
        {
                this.appointmentId = appointmentId;
        }

        public Date getServiceDate()
        {
                return this.serviceDate;
        }

        public void setServiceDate(Date serviceDate)
        {
                this.serviceDate = serviceDate;
        }

        public String getServiceCode()
        {
                return this.serviceCode;
        }

        public void setServiceCode(String serviceCode)
        {
                this.serviceCode = StringUtils.trimToNull(serviceCode);
        }

        public String getDx()
        {
                return this.dx;
        }

        public void setDx(String dx)
        {
                this.dx = StringUtils.trimToNull(dx);
        }

        public String getDx1()
        {
                return this.dx1;
        }

        public void setDx1(String dx)
        {
                this.dx1 = StringUtils.trimToNull(dx);
        }

        public String getDx2()
        {
                return this.dx2;
        }

        public void setDx2(String dx)
        {
                this.dx2 = StringUtils.trimToNull(dx);
        }

        public String getStatus()
        {
                return this.status;
        }

        public void setStatus(String status)
        {
                this.status = status;
        }


	@Override
	public int compareTo(CachedBillingOnItem o)
	{
		return(facilityBillingOnItemPk.getCaisiItemId() - o.facilityBillingOnItemPk.getCaisiItemId());
	}

	@Override
	public FacilityIdIntegerCompositePk getId()
	{
		return facilityBillingOnItemPk;
	}
}
