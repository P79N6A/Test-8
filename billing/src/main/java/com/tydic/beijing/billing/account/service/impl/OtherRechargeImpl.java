 package com.tydic.beijing.billing.account.service.impl;
 
 import com.tydic.beijing.billing.account.biz.RechargeOps;
 import com.tydic.beijing.billing.account.service.OtherRecharge;
 import com.tydic.beijing.billing.dto.RechargeInfo;
 import com.tydic.beijing.billing.dto.RechargeResult;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.Map;
 import java.util.Map.Entry;
 import java.util.Properties;
 import java.util.Set;
 import org.apache.log4j.Logger;
 
 public class OtherRechargeImpl
   implements OtherRecharge
 {
   private static final Logger LOGGER = Logger.getLogger(OtherRechargeImpl.class);
   private static final String RESULT_STATUS_FAIL = "0";
   private static final Map<String, String> rechargeP = new HashMap();
   RechargeOps ops;
 
   static
   {
     Properties p = new Properties();
     try {
       p.load(OtherRechargeImpl.class.getClassLoader().getResourceAsStream("otherRecharge.properties"));
     } catch (Exception ex) {
       LOGGER.error("load recharge.properties error!!!");
       LOGGER.error(ex.getMessage());
       System.exit(1);
     }
     Iterator it = p.entrySet().iterator();
     while (it.hasNext()) {
       Map.Entry entry = (Map.Entry)it.next();
       rechargeP.put(entry.getKey().toString(), entry.getValue().toString());
     }
   }
 
   public RechargeResult otherRecharge(RechargeInfo info)
   {
     LOGGER.debug("Recharge Input:[" + info + "]");
 
     LOGGER.debug("调用飞猫充值的url:" + (String)rechargeP.get("feimaoUrl"));
     return this.ops.recharge145(info, (String)rechargeP.get("feimaoUrl"));
   }
 
   public RechargeOps getOps() {
     return this.ops;
   }
 
   public void setOps(RechargeOps ops) {
     this.ops = ops;
   }
 }
 