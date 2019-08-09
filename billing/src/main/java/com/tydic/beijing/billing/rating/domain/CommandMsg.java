package com.tydic.beijing.billing.rating.domain;

/**
 * 计费命令消息 一个完整的请求内容应该包含一组命令即该命令对应的一组操作数据，有些命令可不带操作数据，如B20,B21。
相关的几个命令同时存在是有优先级，执行的优先级为
返还命令（B04）>扣费命令（B03）>释放命令（B02）>预占命令（B01）> B20（费率查询）> B21（余额查询）>B09试算命令）
 * @author zhanghengbo
 *
 */
public class CommandMsg {

	  //请求组(B01)，预占使用
    private  long m_strReqDuration;            //请求时长
    private  long m_strReqTimes;               //请求使用次数
    private  long m_strReqUpVolume;            //请求预占上行流量
    private  long m_strReqDownVolume;          //请求预占下行流量
    private  long m_strReqTotalVolume;     //请求预占总流量
    //实扣组(B03)
    private  long m_strRealDuration;           //实际使用通话时长
    private  long m_strRealTimes;              //实际使用次数
    private  long m_strRealUpVolume;           //实际上行流量
    private  long m_strRealDownVolume;     //实际下行流量
    private  long m_strRealTotalVolume;        //实际总流量
    //更新使用组(B30)
    private  long m_strUsedDuration;           //使用时长
    private  long m_strUsedTimes;              //使用次数
	private  long m_strUsedUpVolume;           //使用上行流量
    private  long m_strUsedDownVolume;         //使用下行流量
    private  long m_strUsedTotalVolume;        //使用总流量

    private  long m_strUsedUpVolumeFeeLast;           //使用上行流量费率切换点之后的使用量
    private  long m_strUsedDownVolumeFeeLast;         //使用下行流量费率切换点之后的使用量
    private  long m_strUsedTotalVolumeFeeLast;        //使用总流量费率切换点之后的使用量

    private  String m_strReqProductID ="";          //请求的ProductID
    private  String m_strRealProductID ="";         //实扣的ProductID
    private  String m_strUsedProductID ="";          //使用的ProductID
	public long getM_strReqDuration() {
		return m_strReqDuration;
	}
	public void setM_strReqDuration(long m_strReqDuration) {
		this.m_strReqDuration = m_strReqDuration;
	}
 
	public long getM_strReqUpVolume() {
		return m_strReqUpVolume;
	}
	public void setM_strReqUpVolume(long m_strReqUpVolume) {
		this.m_strReqUpVolume = m_strReqUpVolume;
	}
	public long getM_strReqDownVolume() {
		return m_strReqDownVolume;
	}
	public void setM_strReqDownVolume(long m_strReqDownVolume) {
		this.m_strReqDownVolume = m_strReqDownVolume;
	}
	public long getM_strReqTotalVolume() {
		return m_strReqTotalVolume;
	}
	public void setM_strReqTotalVolume(long m_strReqTotalVolume) {
		this.m_strReqTotalVolume = m_strReqTotalVolume;
	}
	public long getM_strRealDuration() {
		return m_strRealDuration;
	}
	public void setM_strRealDuration(long m_strRealDuration) {
		this.m_strRealDuration = m_strRealDuration;
	}
 
	public long getM_strRealUpVolume() {
		return m_strRealUpVolume;
	}
	public void setM_strRealUpVolume(long m_strRealUpVolume) {
		this.m_strRealUpVolume = m_strRealUpVolume;
	}
	public long getM_strRealDownVolume() {
		return m_strRealDownVolume;
	}
	public void setM_strRealDownVolume(long m_strRealDownVolume) {
		this.m_strRealDownVolume = m_strRealDownVolume;
	}
	public long getM_strRealTotalVolume() {
		return m_strRealTotalVolume;
	}
	public void setM_strRealTotalVolume(long m_strRealTotalVolume) {
		this.m_strRealTotalVolume = m_strRealTotalVolume;
	}
	public long getM_strUsedDuration() {
		return m_strUsedDuration;
	}
	public void setM_strUsedDuration(long m_strUsedDuration) {
		this.m_strUsedDuration = m_strUsedDuration;
	}
 
	public long getM_strUsedUpVolume() {
		return m_strUsedUpVolume;
	}
	public void setM_strUsedUpVolume(long m_strUsedUpVolume) {
		this.m_strUsedUpVolume = m_strUsedUpVolume;
	}
	public long getM_strUsedDownVolume() {
		return m_strUsedDownVolume;
	}
	public void setM_strUsedDownVolume(long m_strUsedDownVolume) {
		this.m_strUsedDownVolume = m_strUsedDownVolume;
	}
	public long getM_strUsedTotalVolume() {
		return m_strUsedTotalVolume;
	}
	public void setM_strUsedTotalVolume(long m_strUsedTotalVolume) {
		this.m_strUsedTotalVolume = m_strUsedTotalVolume;
	}
	public long getM_strUsedUpVolumeFeeLast() {
		return m_strUsedUpVolumeFeeLast;
	}
	public void setM_strUsedUpVolumeFeeLast(long m_strUsedUpVolumeFeeLast) {
		this.m_strUsedUpVolumeFeeLast = m_strUsedUpVolumeFeeLast;
	}
	public long getM_strUsedDownVolumeFeeLast() {
		return m_strUsedDownVolumeFeeLast;
	}
	public void setM_strUsedDownVolumeFeeLast(long m_strUsedDownVolumeFeeLast) {
		this.m_strUsedDownVolumeFeeLast = m_strUsedDownVolumeFeeLast;
	}
	public long getM_strUsedTotalVolumeFeeLast() {
		return m_strUsedTotalVolumeFeeLast;
	}
	public void setM_strUsedTotalVolumeFeeLast(long m_strUsedTotalVolumeFeeLast) {
		this.m_strUsedTotalVolumeFeeLast = m_strUsedTotalVolumeFeeLast;
	}
	public String getM_strReqProductID() {
		return m_strReqProductID;
	}
	public void setM_strReqProductID(String m_strReqProductID) {
		this.m_strReqProductID = m_strReqProductID;
	}
	public String getM_strRealProductID() {
		return m_strRealProductID;
	}
	public void setM_strRealProductID(String m_strRealProductID) {
		this.m_strRealProductID = m_strRealProductID;
	}
	public String getM_strUsedProductID() {
		return m_strUsedProductID;
	}
	public void setM_strUsedProductID(String m_strUsedProductID) {
		this.m_strUsedProductID = m_strUsedProductID;
	}
	public long getM_strReqTimes() {
		return m_strReqTimes;
	}
	public void setM_strReqTimes(long m_strReqTimes) {
		this.m_strReqTimes = m_strReqTimes;
	}
	public long getM_strRealTimes() {
		return m_strRealTimes;
	}
	public void setM_strRealTimes(long m_strRealTimes) {
		this.m_strRealTimes = m_strRealTimes;
	}
	public long getM_strUsedTimes() {
		return m_strUsedTimes;
	}
	public void setM_strUsedTimes(long m_strUsedTimes) {
		this.m_strUsedTimes = m_strUsedTimes;
	}
    
    public void print(){
      
    }
}
