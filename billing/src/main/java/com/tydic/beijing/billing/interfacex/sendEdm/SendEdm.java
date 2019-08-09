/**
 * 
 */
package com.tydic.beijing.billing.interfacex.sendEdm;

import java.sql.SQLException;
import java.util.List;

import com.tydic.beijing.billing.dto.SendPayableDto;


/**
 * @author dongxuanyi
 *
 */
public interface SendEdm {

	public boolean sendmail() throws ClassNotFoundException, SQLException;
	
}
