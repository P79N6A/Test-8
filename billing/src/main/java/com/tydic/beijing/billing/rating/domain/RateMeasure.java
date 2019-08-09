package com.tydic.beijing.billing.rating.domain;

public class RateMeasure {
	private long lnDuration = 0;
	private long lnTotalVolume = 0;
	private long lnUpVolume = 0;
	private long lnDownVolume = 0;
	private long lnTimes = 0;
	private long lnMoney = 0;
	private long lnLastTotalVolume = 0;
	private long lnLastUpVolume = 0;
	private long lnLastDownVolume = 0;

    //已经批过价的使用量
	private long lnChargedDuration = 0;
	private long lnChargedTotalVolume = 0;
	private long lnChargedUpVolume = 0;
	private long lnChargedDownVolume = 0;
	private long lnChargedTimes = 0;
	private long lnChargedMoney = 0;
	private long lnChargedLastTotalVolume = 0;
	private long lnChargedLastUpVolume = 0;
	private long lnChargedLastDownVolume = 0;
    
    //未使用的使用量
	private long lnUnusedDuration = 0;
	private long lnUnusedTotalVolume = 0;
	private long lnUnusedUpVolume = 0;
	private long lnUnusedDownVolume = 0;
	private long lnUnusedTimes = 0;
	private long lnUnusedMoney = 0;
	private long lnUnusedLastTotalVolume = 0;
	private long lnUnusedLastUpVolume = 0;
	private long lnUnusedLastDownVolume = 0;
	
	private int m_iUsedMeasureDomain=-1; ////这个是做什么的？
	
	/**
	 * 按时长       01
		按流量       02
		按次数       03
		上行流量     04
		下行流量     05
		按金钱       07
	 * @param measure
	 * @return
	 */
	public long getUnchargeDosage(String measure){
		long dosage=0;
		
		if(measure.equals(RatingMacro.SECTION_DOMAIN_DURATION)){//时长
			dosage=lnDuration-lnChargedDuration;
		}else if(measure.equals(RatingMacro.SECTION_DOMAIN_TOTALVOLUME)){
			dosage=lnTotalVolume-lnChargedTotalVolume;
		}else if(measure.equals(RatingMacro.SECTION_DOMAIN_TIMES)){
			dosage=lnTimes-lnChargedTimes;
		}else if(measure.equals(RatingMacro.SECTION_DOMAIN_UPVOLUME)){
			dosage=lnUpVolume-lnChargedUpVolume;
		}else if(measure.equals(RatingMacro.SECTION_DOMAIN_DOWNVOLUME)){
			dosage=lnDownVolume-lnChargedDownVolume;
		}else if(measure.equals(RatingMacro.SECTION_DOMAIN_MONEY)){
			dosage=lnMoney-lnChargedMoney;
		}else
			return -1;
		if(dosage<0){
			dosage=0;
		}
		return dosage;
	}
	
	public void complete(){
     String domain=""+m_iUsedMeasureDomain;
		
		if(domain.equals( RatingMacro.SECTION_DOMAIN_DURATION)){
			lnChargedDuration =lnDuration;
		}else if(domain.equals( RatingMacro.SECTION_DOMAIN_TIMES)){
			lnChargedTimes = lnTimes;
		}else if(domain.equals(RatingMacro.SECTION_DOMAIN_TOTALVOLUME)){
			lnChargedTotalVolume = lnTotalVolume;
		}else if(domain.equals(RatingMacro.SECTION_DOMAIN_UPVOLUME)){
			lnChargedUpVolume =lnUpVolume ;
		}else if(domain.equals(RatingMacro.SECTION_DOMAIN_DOWNVOLUME)){
			lnChargedDownVolume = lnDownVolume;
		}else if(domain.equals(RatingMacro.SECTION_DOMAIN_MONEY)){
			lnChargedMoney = lnMoney;
		}
	}
	
	
	
	public void updateChargedDosage(long value){
		 String domain=""+m_iUsedMeasureDomain;
			
			if(domain.equals( RatingMacro.SECTION_DOMAIN_DURATION)){
				lnChargedDuration =value;
			}else if(domain.equals( RatingMacro.SECTION_DOMAIN_TIMES)){
				lnChargedTimes = value;
			}else if(domain.equals(RatingMacro.SECTION_DOMAIN_TOTALVOLUME)){
				lnChargedTotalVolume = value;
			}else if(domain.equals(RatingMacro.SECTION_DOMAIN_UPVOLUME)){
				lnChargedUpVolume =value ;
			}else if(domain.equals(RatingMacro.SECTION_DOMAIN_DOWNVOLUME)){
				lnChargedDownVolume = value;
			}else if(domain.equals(RatingMacro.SECTION_DOMAIN_MONEY)){
				lnChargedMoney = value;
			}
	}
	

	public long getChargedDosage(){
		 String domain=""+m_iUsedMeasureDomain;
		 long value=0L;
			
			if(domain.equals( RatingMacro.SECTION_DOMAIN_DURATION)){
				value = lnChargedDuration ;
			}else if(domain.equals( RatingMacro.SECTION_DOMAIN_TIMES)){
				value = lnChargedTimes ;
			}else if(domain.equals(RatingMacro.SECTION_DOMAIN_TOTALVOLUME)){
				value = lnChargedTotalVolume ;
			}else if(domain.equals(RatingMacro.SECTION_DOMAIN_UPVOLUME)){
				value = lnChargedUpVolume ;
			}else if(domain.equals(RatingMacro.SECTION_DOMAIN_DOWNVOLUME)){
				value = lnChargedDownVolume ;
			}else if(domain.equals(RatingMacro.SECTION_DOMAIN_MONEY)){
				value = lnChargedMoney ;
			}
			
			return value;
	}
	


	public boolean isNoLeftDosage(){
		String domain=""+m_iUsedMeasureDomain;
		
		if(domain.equals( RatingMacro.SECTION_DOMAIN_DURATION)){
			return lnDuration<=lnChargedDuration;
		}else if(domain.equals( RatingMacro.SECTION_DOMAIN_TIMES)){
			return lnTimes <= lnChargedTimes;
		}else if(domain.equals(RatingMacro.SECTION_DOMAIN_TOTALVOLUME)){
			return lnTotalVolume <= lnChargedTotalVolume && lnLastTotalVolume <= lnChargedLastTotalVolume;
		}else if(domain.equals(RatingMacro.SECTION_DOMAIN_UPVOLUME)){
			return  lnUpVolume <= lnChargedUpVolume && lnLastUpVolume <= lnChargedLastUpVolume;
		}else if(domain.equals(RatingMacro.SECTION_DOMAIN_DOWNVOLUME)){
			return lnDownVolume <= lnChargedDownVolume && lnLastDownVolume <= lnChargedLastDownVolume;
		}else if(domain.equals(RatingMacro.SECTION_DOMAIN_MONEY)){
			return lnMoney <= lnChargedMoney;
		}else if(domain.equals(RatingMacro.SECTION_DOMAIN_NONE)){
			return false;
		}else{
			return false;
		}
       
	}
	
	
	public long getUnchargedLastDosage(String measure){
		long unChargedLastDosage=0;
		if(measure.equals("2")){
			unChargedLastDosage=lnLastTotalVolume-lnChargedLastTotalVolume;
		}else if(measure.equals("4")){
			unChargedLastDosage=lnLastUpVolume-lnChargedLastUpVolume;
		}else if(measure.equals("5")){
			unChargedLastDosage=lnLastDownVolume-lnChargedLastDownVolume;
		}
		if(unChargedLastDosage<0){
			unChargedLastDosage=0;
		}
		return unChargedLastDosage;
	}
	
	public int addChargedDosage( long change_)
    {
		String measure_ = m_iUsedMeasureDomain+"";
        
        if( measure_ .equals("1") ) //时长
            lnChargedDuration += change_;
        else if( measure_ .equals("2") ) //总流量
            lnChargedTotalVolume += change_;
        else if( measure_ .equals("3") ) //次
            lnChargedTimes += change_;
        else if( measure_ .equals("4") ) //上行流量
            lnChargedUpVolume += change_;
        else if( measure_ .equals("5") ) //下行流量
            lnChargedDownVolume += change_;
        else if( measure_ .equals("7") ) //money
            lnChargedMoney += change_;
        else
            return -1;

        return 0;        
    }
	
	public int addUnusedDosage(String measure_, long change_)
    {
        
        if( measure_ .equals("1") ) //时长
            lnUnusedDuration += change_;
        else if( measure_ .equals("2") ) //总流量
            lnUnusedTotalVolume += change_;
        else if( measure_ .equals("3") ) //次
            lnUnusedTimes += change_;
        else if( measure_ .equals("4") ) //上行流量
            lnUnusedUpVolume += change_;
        else if( measure_ .equals("5") ) //下行流量
            lnUnusedDownVolume += change_;
        else if( measure_ .equals("7") ) //money
            lnUnusedMoney += change_;
        else
            return -1;

        return 0;        
    } 
	
	public void addChargedLastDosage(String measure_, long dosage_)
    {
        if( measure_ .equals("2") )
            lnChargedLastTotalVolume += dosage_;
        else if( measure_ .equals("4") )
            lnChargedLastUpVolume += dosage_;
        else if( measure_ .equals("5") )
            lnChargedLastDownVolume += dosage_;
       
    }
	
	public void addUnusedLastDosage(String measure_, long dosage_)
    {
        if( measure_ .equals("2") )
            lnUnusedLastTotalVolume += dosage_;
        else if( measure_ .equals("4") )
            lnUnusedLastUpVolume += dosage_;
        else if( measure_ .equals("5") )
            lnUnusedLastDownVolume += dosage_;
       
    }
	

	
	public long getLnDuration() {
		return lnDuration;
	}



	public void setLnDuration(long lnDuration) {
		this.lnDuration = lnDuration;
	}



	public long getLnTotalVolume() {
		return lnTotalVolume;
	}



	public void setLnTotalVolume(long lnTotalVolume) {
		this.lnTotalVolume = lnTotalVolume;
	}



	public long getLnUpVolume() {
		return lnUpVolume;
	}



	public void setLnUpVolume(long lnUpVolume) {
		this.lnUpVolume = lnUpVolume;
	}



	public long getLnDownVolume() {
		return lnDownVolume;
	}



	public void setLnDownVolume(long lnDownVolume) {
		this.lnDownVolume = lnDownVolume;
	}



	public long getLnTimes() {
		return lnTimes;
	}



	public void setLnTimes(long lnTimes) {
		this.lnTimes = lnTimes;
	}



	public long getLnMoney() {
		return lnMoney;
	}



	public void setLnMoney(long lnMoney) {
		this.lnMoney = lnMoney;
	}



	public long getLnLastTotalVolume() {
		return lnLastTotalVolume;
	}



	public void setLnLastTotalVolume(long lnLastTotalVolume) {
		this.lnLastTotalVolume = lnLastTotalVolume;
	}



	public long getLnLastUpVolume() {
		return lnLastUpVolume;
	}



	public void setLnLastUpVolume(long lnLastUpVolume) {
		this.lnLastUpVolume = lnLastUpVolume;
	}



	public long getLnLastDownVolume() {
		return lnLastDownVolume;
	}



	public void setLnLastDownVolume(long lnLastDownVolume) {
		this.lnLastDownVolume = lnLastDownVolume;
	}



	public long getLnChargedDuration() {
		return lnChargedDuration;
	}



	public void setLnChargedDuration(long lnChargedDuration) {
		this.lnChargedDuration = lnChargedDuration;
	}



	public long getLnChargedTotalVolume() {
		return lnChargedTotalVolume;
	}



	public void setLnChargedTotalVolume(long lnChargedTotalVolume) {
		this.lnChargedTotalVolume = lnChargedTotalVolume;
	}



	public long getLnChargedUpVolume() {
		return lnChargedUpVolume;
	}



	public void setLnChargedUpVolume(long lnChargedUpVolume) {
		this.lnChargedUpVolume = lnChargedUpVolume;
	}



	public long getLnChargedDownVolume() {
		return lnChargedDownVolume;
	}



	public void setLnChargedDownVolume(long lnChargedDownVolume) {
		this.lnChargedDownVolume = lnChargedDownVolume;
	}



	public long getLnChargedTimes() {
		return lnChargedTimes;
	}



	public void setLnChargedTimes(long lnChargedTimes) {
		this.lnChargedTimes = lnChargedTimes;
	}



	public long getLnChargedMoney() {
		return lnChargedMoney;
	}



	public void setLnChargedMoney(long lnChargedMoney) {
		this.lnChargedMoney = lnChargedMoney;
	}



	public long getLnChargedLastTotalVolume() {
		return lnChargedLastTotalVolume;
	}



	public void setLnChargedLastTotalVolume(long lnChargedLastTotalVolume) {
		this.lnChargedLastTotalVolume = lnChargedLastTotalVolume;
	}



	public long getLnChargedLastUpVolume() {
		return lnChargedLastUpVolume;
	}



	public void setLnChargedLastUpVolume(long lnChargedLastUpVolume) {
		this.lnChargedLastUpVolume = lnChargedLastUpVolume;
	}



	public long getLnChargedLastDownVolume() {
		return lnChargedLastDownVolume;
	}



	public void setLnChargedLastDownVolume(long lnChargedLastDownVolume) {
		this.lnChargedLastDownVolume = lnChargedLastDownVolume;
	}



	public long getLnUnusedDuration() {
		return lnUnusedDuration;
	}



	public void setLnUnusedDuration(long lnUnusedDuration) {
		this.lnUnusedDuration = lnUnusedDuration;
	}



	public long getLnUnusedTotalVolume() {
		return lnUnusedTotalVolume;
	}



	public void setLnUnusedTotalVolume(long lnUnusedTotalVolume) {
		this.lnUnusedTotalVolume = lnUnusedTotalVolume;
	}



	public long getLnUnusedUpVolume() {
		return lnUnusedUpVolume;
	}



	public void setLnUnusedUpVolume(long lnUnusedUpVolume) {
		this.lnUnusedUpVolume = lnUnusedUpVolume;
	}



	public long getLnUnusedDownVolume() {
		return lnUnusedDownVolume;
	}



	public void setLnUnusedDownVolume(long lnUnusedDownVolume) {
		this.lnUnusedDownVolume = lnUnusedDownVolume;
	}



	public long getLnUnusedTimes() {
		return lnUnusedTimes;
	}



	public void setLnUnusedTimes(long lnUnusedTimes) {
		this.lnUnusedTimes = lnUnusedTimes;
	}



	public long getLnUnusedMoney() {
		return lnUnusedMoney;
	}



	public void setLnUnusedMoney(long lnUnusedMoney) {
		this.lnUnusedMoney = lnUnusedMoney;
	}



	public long getLnUnusedLastTotalVolume() {
		return lnUnusedLastTotalVolume;
	}



	public void setLnUnusedLastTotalVolume(long lnUnusedLastTotalVolume) {
		this.lnUnusedLastTotalVolume = lnUnusedLastTotalVolume;
	}



	public long getLnUnusedLastUpVolume() {
		return lnUnusedLastUpVolume;
	}



	public void setLnUnusedLastUpVolume(long lnUnusedLastUpVolume) {
		this.lnUnusedLastUpVolume = lnUnusedLastUpVolume;
	}



	public long getLnUnusedLastDownVolume() {
		return lnUnusedLastDownVolume;
	}



	public void setLnUnusedLastDownVolume(long lnUnusedLastDownVolume) {
		this.lnUnusedLastDownVolume = lnUnusedLastDownVolume;
	}



	public int getM_iUsedMeasureDomain() {
		return m_iUsedMeasureDomain;
	}



	public void setM_iUsedMeasureDomain(int m_iUsedMeasureDomain) {
		this.m_iUsedMeasureDomain = m_iUsedMeasureDomain;
	}



	@Override
	public String toString() {
		String ret="lnDuration["+lnDuration+"],lnTotalVolume["+lnTotalVolume+"],lnUpVolume["+lnUpVolume+"],lnDownVolume["+lnDownVolume+"],lnTimes["+
				lnTimes+"],lnMoney["+lnMoney+"],lnLastTotalVolume["+lnLastTotalVolume+"],lnLastUpVolume["+lnLastUpVolume+"],lnLastDownVolume["+
				lnLastDownVolume+"],\nlnChargedDuration["+lnChargedDuration+"],lnChargedTotalVolume"+lnChargedTotalVolume+"],lnChargedUpVolume"+
				lnChargedUpVolume+"],lnChargeDownVolume["+lnChargedDownVolume+"],lnChargedTimes["+lnChargedTimes+"],lnChargedMoney["+lnChargedMoney+
				"],lnChargeLastTotalVolume["+lnChargedLastTotalVolume+"],lnChargedLastUpVolume["+lnChargedLastUpVolume+"],lnChargedLastDownVolume["+
				lnChargedLastDownVolume+"]\nlnUnusedDuration["+lnUnusedDuration+"],lnUnusedTotalVolume["+lnUnusedTotalVolume+"],lnUnusedUpVolume["+lnUnusedUpVolume+"],"+
				"lnUnusedDownVolume["+lnUnusedDownVolume+"],lnUnusedTimes["+lnUnusedTimes+"],lnUnusedMoney["+lnUnusedMoney+"],lnUnusedLastTotalVolume["+
				lnUnusedLastTotalVolume+"],lnUnusedLastUpVolume["+lnUnusedLastUpVolume+"],lnUnusedLastDownVolume["+lnUnusedLastDownVolume+"]";
		
		return ret;
	}
	
	
	public RateMeasure(){
		
	}
	
	public RateMeasure(RateMeasure rm){
		  this.lnDuration= rm.getLnDuration() ;
			this.lnTotalVolume= rm.getLnTotalVolume() ;
			this.lnUpVolume= rm.getLnUpVolume() ;
			this.lnDownVolume= rm.getLnDownVolume() ;
			this.lnTimes = rm.getLnTimes() ;
			this.lnMoney = rm.getLnMoney() ;
			this.lnLastTotalVolume = rm.getLnLastTotalVolume() ;
			this.lnLastUpVolume = rm.getLnLastUpVolume() ;
			this.lnLastDownVolume = rm.getLnLastDownVolume() ;
			this.lnChargedDuration = rm.getLnChargedDuration() ;
			this.lnChargedTotalVolume = rm.getLnChargedTotalVolume() ;
			this.lnChargedUpVolume = rm.getLnChargedUpVolume() ;
			this.lnChargedDownVolume = rm.getLnChargedDownVolume() ;
			this.lnChargedTimes = rm.getLnChargedTimes() ;
			this.lnChargedMoney = rm.getLnChargedMoney() ;
			this.lnChargedLastTotalVolume = rm.getLnChargedLastTotalVolume() ;
			this.lnChargedLastUpVolume = rm.getLnChargedLastUpVolume() ;
			this.lnChargedLastDownVolume = rm.getLnChargedLastDownVolume() ;
			this.lnUnusedDuration = rm.getLnUnusedDuration() ;
			this.lnUnusedTotalVolume = rm.getLnUnusedTotalVolume() ;
			this.lnUnusedUpVolume = rm.getLnUnusedUpVolume() ;
			this.lnUnusedDownVolume = rm.getLnUnusedDownVolume() ;
			this.lnUnusedTimes = rm.getLnUnusedTimes() ;
			this.lnUnusedMoney = rm.getLnUnusedMoney() ;
			this.lnUnusedLastTotalVolume = rm.getLnUnusedLastTotalVolume() ;
			this.lnUnusedLastUpVolume = rm.getLnUnusedLastUpVolume() ;
			this.lnUnusedLastDownVolume = rm.getLnUnusedLastDownVolume() ;
			this.m_iUsedMeasureDomain =  rm.getM_iUsedMeasureDomain() ;
	}

	public void initChargedDetail() {
		this.lnChargedDuration = 0L;
		this.lnChargedTotalVolume = 0L;
		this.lnChargedUpVolume = 0L;
		this.lnChargedDownVolume = 0L;
		this.lnChargedTimes = 0L;
		this.lnChargedMoney = 0L;
		this.lnChargedLastTotalVolume = 0L;
		this.lnChargedLastUpVolume = 0L;
		this.lnChargedLastDownVolume = 0L;		
	}
 
	
}
