export interface Statistic{
  requestCount:number;
  request200Count:number;
  request4XXCount:number;
  request5XXCount:number;
  totalRequestTimeInSeconds:number;
  maxResponseTimeInSeconds:number;
}
