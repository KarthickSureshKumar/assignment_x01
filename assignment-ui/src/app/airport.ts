import { Pageable } from './pageable';
import { Embedded } from './embedded';

export interface Airport{
  _embedded: Embedded;
  page: Pageable
}
