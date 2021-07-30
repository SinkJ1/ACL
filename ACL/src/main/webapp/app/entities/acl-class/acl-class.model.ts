import { IAclObjectIdentity } from 'app/entities/acl-object-identity/acl-object-identity.model';

export interface IAclClass {
  id?: number;
  className?: string;
  classIdType?: string;
  aclObjectIdentities?: IAclObjectIdentity[] | null;
}

export class AclClass implements IAclClass {
  constructor(
    public id?: number,
    public className?: string,
    public classIdType?: string,
    public aclObjectIdentities?: IAclObjectIdentity[] | null
  ) {}
}

export function getAclClassIdentifier(aclClass: IAclClass): number | undefined {
  return aclClass.id;
}
