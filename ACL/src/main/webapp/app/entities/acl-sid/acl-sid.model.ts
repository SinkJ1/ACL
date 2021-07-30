import { IAclEntry } from 'app/entities/acl-entry/acl-entry.model';

export interface IAclSid {
  id?: number;
  principal?: boolean;
  sid?: string;
  aclEntries?: IAclEntry[] | null;
}

export class AclSid implements IAclSid {
  constructor(public id?: number, public principal?: boolean, public sid?: string, public aclEntries?: IAclEntry[] | null) {
    this.principal = this.principal ?? false;
  }
}

export function getAclSidIdentifier(aclSid: IAclSid): number | undefined {
  return aclSid.id;
}
