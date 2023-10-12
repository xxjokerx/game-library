import {Injectable, OnDestroy} from '@angular/core';
import {Subscription} from 'rxjs';

export const CREATION = 'creation';
export const EDITION = 'edition';
export const NAV = 'navigation';


@Injectable({providedIn: 'root'})
export class WrapperService implements OnDestroy {

  // put it where needed
  public MODE_NAMES = {};

  mode: string;
  entity: string;
  subscription: Subscription;

  constructor() {
    this.MODE_NAMES[CREATION] = 'Création';
    this.MODE_NAMES[EDITION] = 'Édition';
    this.MODE_NAMES[NAV] = 'Navigation';

    /* this method seems not secured */
    // this.subscription = this.router.events
    //   .pipe(filter(event => event instanceof NavigationStart))
    //   .subscribe(() => {
    //     if (!this.router.url.startsWith('/admin//')) {
    //       this.mode = NAV;
    //     }
    //   });
  }

  ngOnDestroy(): void {
    // this.subscription.unsubscribe();
  }

  getModeName(): string {
    return this.MODE_NAMES[this.mode];
  }
}
