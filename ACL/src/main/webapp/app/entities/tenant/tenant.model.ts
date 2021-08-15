export interface ITenant {
  id?: number;
  tenantId?: string | null;
  schema?: string | null;
}

export class Tenant implements ITenant {
  constructor(public id?: number, public tenantId?: string | null, public schema?: string | null) {}
}

export function getTenantIdentifier(tenant: ITenant): number | undefined {
  return tenant.id;
}
