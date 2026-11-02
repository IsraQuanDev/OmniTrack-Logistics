import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './components/header/header.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { LiveMapComponent } from './components/live-map/live-map.component';
import { RouteStudioComponent } from './components/route-studio/route-studio.component';
import { FleetManagerComponent } from './components/fleet-manager/fleet-manager.component';
import { AlertCenterComponent } from './components/alert-center/alert-center.component';
import { EndpointRunnerComponent } from './components/endpoint-runner/endpoint-runner.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    HeaderComponent,
    SidebarComponent,
    LiveMapComponent,
    RouteStudioComponent,
    FleetManagerComponent,
    AlertCenterComponent,
    EndpointRunnerComponent
  ],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  activeTab = 'map';

  onTabChanged(tab: string): void {
    this.activeTab = tab;
  }
}
