
## Approach
- We are processing incoming quote events and saving candlestick for 1 minute time interval.
- This preprocessing will provide low latency and high response time when **getCandlesticks** rest will be called for any isin.
- Here we are using in-memory data storage for simplicity but in real time scenario we can use any key-value based NOSQL database.

## Assumptions

### /getCandlesticks REST API
- Returning last 30 minutes history including current minute.
- If no price update happened for last 30 minutes then returning empty list which results into no_data_for_isin response 
  (Can be improved by adding proper response message)
- If the price update events started happening for any isin within 30 minutes then it will return history from which first quote 
  event happened.
- For any missing time interval in will copy last record of candlestick.
- If isin data is not present then returning empty list which results into no_data_for_isin response.

### Process quote events
- While processing quote events we are saving candlesticks for input isin.
- If price update event belongs to same time interval we are updating old candlestick entry.
- If no price update events received for some time intervals then copy last candlestick record.

## Future Scope
-- Add data storage. 
-- Add Integration test cases.
-- Add proper validation error message responses for better user experience.

