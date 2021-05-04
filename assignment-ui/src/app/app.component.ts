import { Statistic } from './statistic';
import { FareDetails } from './fareDetails';
import { Fare } from './fare';
import { Airport } from './airport';
import { Location } from './location';
import { TravelappService } from './travelapp.service';
import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

class DataTablesResponse {
  data: any[];
  draw: number;
  recordsFiltered: number;
  recordsTotal: number;
  constructor(){
    this.data = [];
    this.draw= 0;
    this.recordsFiltered = 0;
    this.recordsTotal = 0;
  }
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  public location: Location[] = [];
  public originResults: Location[] = [];
  public destResults: Location[] = [];
  public dtOptions: DataTables.Settings = {};
  selectedOrgin:any;
  selectedDestination:any;
  fareDetails:any;
  statisticJson:any;
  statistic:Statistic = {
    requestCount:0,
    request200Count:0,
    request4XXCount:0,
    request5XXCount:0,
    totalRequestTimeInSeconds:0,
    maxResponseTimeInSeconds:0
  };
  constructor(private travelapp: TravelappService){
  }
  ngOnInit(){
    const self = this;
    this.dtOptions = {
      pagingType: 'full_numbers',
      pageLength: 10,
      lengthMenu : [10,25,50],
      processing: true,
      serverSide: true,
      ajax: (dataTablesParameters: any, callback) => {
        this.travelapp.getAllAirport(dataTablesParameters?.length,dataTablesParameters?.draw,dataTablesParameters?.search?.value).subscribe(
          (response:Airport)=>{
            this.location = response._embedded?.locations;
            callback({
              recordsTotal: response.page?.totalElements,
              recordsFiltered: response.page?.totalElements,
              draw: response.page?.number,
              data: []
            });
          },
          (error:HttpErrorResponse)=>{
            alert(error.message);
          }
        );
      }
    };
    this.getStats();
  }

  public selectedItem(location:Location,mode:string):void{
    if(location){
      location.displayText = `${location.name} (${location.code})`
      if(mode === 'origin'){
        this.selectedOrgin = location;
        this.originResults = [];
      }else if (mode === 'dest'){
        this.selectedDestination = location;
        this.destResults = [];
      }
   }
  }

  public getFares(fare:Fare):void{
    if(fare?.origin.trim().indexOf(this.selectedOrgin?.name.trim())!==-1 && fare?.dest.trim().indexOf(this.selectedDestination?.name.trim())!==-1){
      this.travelapp.getFare(this.selectedOrgin?.code,this.selectedDestination?.code).subscribe(
        (response:FareDetails)=>{
          this.fareDetails = response;
          this.originResults = [];
          this.destResults = [];

        },
        (error:HttpErrorResponse)=>{
          alert(error.message);
        }
      );
    }
  }

  public searchCode(term:string,mode:String):void{
    if(term){
      this.travelapp.getAllAirport(0,0,term).subscribe(
        (response:Airport)=>{
          if(mode === 'origin'){
            this.originResults = response._embedded?.locations;
          }else if (mode === 'dest'){
            this.destResults = response._embedded?.locations;
          }
        },
        (error:HttpErrorResponse)=>{
          alert(error.message);
        });

    }else{
      if(mode === 'origin'){
        this.originResults = [];
      }
      else if (mode === 'dest'){
        this.destResults = [];
      }
    }
  }

  public getStats():void{
    this.travelapp.getStatistics().subscribe(
      (response:any)=>{
        let measurements = response?.measurements;
        let availableTags = response?.availableTags;
        availableTags?.forEach((element: any) => {
            if(element?.tag === "status"){
                let allStatus = element.values;
                allStatus?.forEach((status: any) => {
                  this.travelapp.getStatistics(status).subscribe(
                    (res:any)=>{
                      let statusMeasurements = res?.measurements;
                      let count = statusMeasurements.length!==0?statusMeasurements[0].value:0;
                      if(status === "400" || status == "404" || status == "403"){
                        this.statistic.request4XXCount = count;
                      }else if (status === "500" || status === "502"){
                        this.statistic.request5XXCount = count;
                      }else if(status === "200"){
                        this.statistic.request200Count = count;
                      }
                    },
                    (err:HttpErrorResponse)=>{
                      console.log("Problem while fetching metrics... "+err.message);
                    }
                  );
                });
            }
        });
        measurements?.forEach((ele: { statistic: string; value: number; }) => {
          if(ele?.statistic === "COUNT" ){
            this.statistic.requestCount = ele.value;
          }else if(ele?.statistic === "TOTAL_TIME"){
            this.statistic.totalRequestTimeInSeconds = ele.value;
          }else if(ele?.statistic === "MAX"){
            this.statistic.maxResponseTimeInSeconds = ele.value;
          }
        });
        this.statisticJson =  JSON.stringify(this.statistic,undefined, 4);
      },
      (error:HttpErrorResponse)=>{
        alert(error.message);
      }
    );
  }
}
