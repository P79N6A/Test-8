/**
 * 
 */
package com.tydic.beijing.billing.interfacex.service;

import com.tydic.beijing.billing.dto.CurrentConsume;
import com.tydic.beijing.billing.dto.CurrentConsumeQueryInfo;

/**
 * @author sung
 *
 */
public interface QueryCurrentConsume {

	public CurrentConsume query(CurrentConsumeQueryInfo info);
}
