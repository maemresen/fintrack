import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import {of} from "rxjs";
import {ActivatedRoute} from "@angular/router";

const activatedRouteMock = {
  snapshot: {
    paramMap: {
      get: (name: string) => '/'
    }
  },
  queryParams: of({ /* your query params */ }),
  params: of({ /* your route params */ })
};


describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [
        { provide: ActivatedRoute, useValue: activatedRouteMock }
      ]

    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have the 'fintrack-fe' title`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('fintrack-fe');
  });

  it('should render title', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('Hello, fintrack-fe');
  });
});
