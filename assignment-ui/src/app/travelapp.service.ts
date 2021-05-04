import { FareDetails } from './fareDetails';
import { Airport } from './airport';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class TravelappService {

  constructor(private http:HttpClient) { }

  public getAllAirport(size?:number,page?:number,term?:string): Observable<Airport> {
    let query;
    if(term){
      return this.http.get<Airport>(`/airport/all?term=${term}`);
    }else if(size && page && size >= 0 && page >= 0){
      query = `size=${size}&page=${page}`;
      return this.http.get<Airport>(`/airport/all?${query}`);
    }
    return this.http.get<Airport>('/airport/all');
  }

  public getFare(origin:string,dest:string) : Observable<FareDetails> {
      return this.http.get<FareDetails>(`/fares/${origin}/${dest}`);
  }

  public getStatistics(status?:number) : Observable<any>{
    if(status){
      return this.http.get<any>(`/actuator/metrics/http.server.requests?tag=status:${status}`)
    }
    return this.http.get<any>('/actuator/metrics/http.server.requests');
  }

}
