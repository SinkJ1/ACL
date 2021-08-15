import { IAclEntry } from 'app/entities/acl-entry/acl-entry.model';

export interface IAclSid {
  id?: number;
  sid?: string;
  aclEntries?: IAclEntry[] | null;
}

export class AclSid implements IAclSid {
  constructor(public id?: number, public sid?: string, public aclEntries?: IAclEntry[] | null) {}
}

export function getAclSidIdentifier(aclSid: IAclSid): number | undefined {
  return aclSid.id;
}
