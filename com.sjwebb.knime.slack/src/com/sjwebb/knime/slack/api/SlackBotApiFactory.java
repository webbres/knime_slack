package com.sjwebb.knime.slack.api;

import java.util.Map;
import java.util.TreeMap;

import com.sjwebb.knime.slack.exception.KnimeSlackException;

public class SlackBotApiFactory 
{
	
	private static Map<String, SlackBotApi> INSTANCES = new TreeMap<String, SlackBotApi>();
//	private static Map<String, MemoryMetricsDatastore> STORE = new TreeMap<String, MemoryMetricsDatastore>();
	
	/**
	 * Issues a {@link SlackBotApi} backed by the given token. If an API instance exists for the given
	 * token it is returned otherwise a new instance is configured
	 * @param token
	 * @return
	 * @throws KnimeSlackException 
	 */
	public static SlackBotApi getInstanceForToken(String token) throws KnimeSlackException 
	{
		if(token == null || token.equals(""))
			throw new KnimeSlackException("Token cannot be null or empty");
		
		if(!INSTANCES.containsKey(token)) 
		{
			SlackBotApi api;
			try 
			{
				api = new SlackBotApi(token);
			} catch (Exception e) {
				e.printStackTrace();
				throw new KnimeSlackException(e);
			}
//			api.setDataStore(getStoreForToken(token));
			INSTANCES.put(token, api);
		}
		
		return INSTANCES.get(token);
	}
	
	
	
	public static Map<String, SlackBotApi> getINSTANCES() 
	{
		return INSTANCES;
	}





	//	/**
//	 * Get the metric store for the provided token. If one does not exist it will be created and stored
//	 * prior to returning.
//	 * 
//	 * @param token
//	 * @return
//	 */
//	public static MemoryMetricsDatastore getStoreForToken(String token) {
//		
//		if(!STORE.containsKey(token)) {
//			STORE.put(token, new MemoryMetricsDatastore(1));
//		}
//		
//		return STORE.get(token);
//	}
//
//	
//	public static Map<String, MemoryMetricsDatastore> getMetrics() 
//	{
//		return STORE;
//	}
//		
	public static void clear()
	{
		INSTANCES.clear();
//		STORE.clear();
	}
}
