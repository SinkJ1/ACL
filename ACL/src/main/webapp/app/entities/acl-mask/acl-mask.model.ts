import { IAclEntry } from 'app/entities/acl-entry/acl-entry.model';

export interface IAclMask {
  id?: number;
  name?: string | null;
  aclEntries?: IAclEntry[] | null;
}

export class AclMask implements IAclMask {
  constructor(public id?: number, public name?: string | null, public aclEntries?: IAclEntry[] | null) {}
}

export function getAclMaskIdentifier(aclMask: IAclMask): number | undefined {
  return aclMask.id;
}
