import { VehicleSearchInterface } from "@/interfaces/vehicleSearchInterface";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

type Props = {
  filters: Partial<VehicleSearchInterface>;
  onFilterChange: (filters: Partial<VehicleSearchInterface>) => void;
  onSearch: () => void;
};

export const FilterSection = ({ filters, onFilterChange, onSearch }: Props) => {
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    onFilterChange({ ...filters, [name]: value });
  };

  const handleSelectChange = (name: string, value: string | null) => {
    onFilterChange({ ...filters, [name]: value });
  };

  return (
    <Card className="shadow-sm border-slate-200 sticky top-24">
      <CardHeader>
        <CardTitle>Filters</CardTitle>
      </CardHeader>
      <CardContent className="space-y-6">
        <div className="space-y-2">
          <label htmlFor="brand" className="text-sm font-medium text-slate-900">
            Brand
          </label>
          <Input
            id="brand"
            name="brand"
            placeholder="e.g. Yamaha"
            value={filters.brand || ""}
            onChange={handleInputChange}
          />
        </div>
        <div className="space-y-2">
          <label htmlFor="color" className="text-sm font-medium text-slate-900">
            Color
          </label>
          <Input
            id="color"
            name="color"
            placeholder="e.g. Blue"
            value={filters.color || ""}
            onChange={handleInputChange}
          />
        </div>
        <div className="grid grid-cols-2 gap-4">
          <div className="space-y-2">
            <label
              htmlFor="yearMin"
              className="text-sm font-medium text-slate-900"
            >
              Min Year
            </label>
            <Input
              id="yearMin"
              name="yearMin"
              type="number"
              placeholder="1990"
              value={filters.yearMin || ""}
              onChange={handleInputChange}
            />
          </div>
          <div className="space-y-2">
            <label
              htmlFor="yearMax"
              className="text-sm font-medium text-slate-900"
            >
              Max Year
            </label>
            <Input
              id="yearMax"
              name="yearMax"
              type="number"
              placeholder="2024"
              value={filters.yearMax || ""}
              onChange={handleInputChange}
            />
          </div>
        </div>
        <div className="grid grid-cols-2 gap-4">
          <div className="space-y-2">
            <label
              htmlFor="priceMin"
              className="text-sm font-medium text-slate-900"
            >
              Min Price
            </label>
            <Input
              id="priceMin"
              name="priceMin"
              type="number"
              placeholder="1000"
              value={filters.priceMin || ""}
              onChange={handleInputChange}
            />
          </div>
          <div className="space-y-2">
            <label
              htmlFor="priceMax"
              className="text-sm font-medium text-slate-900"
            >
              Max Price
            </label>
            <Input
              id="priceMax"
              name="priceMax"
              type="number"
              placeholder="50000"
              value={filters.priceMax || ""}
              onChange={handleInputChange}
            />
          </div>
        </div>
        <div className="space-y-2">
          <label
            htmlFor="sortBy"
            className="text-sm font-medium text-slate-900"
          >
            Sort By
          </label>
          <Select
            name="sortBy"
            onValueChange={(value) => handleSelectChange("sortBy", value)}
            value={filters.sortBy}
          >
            <SelectTrigger id="sortBy">
              <SelectValue placeholder="Sort by" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="price">Price</SelectItem>
              <SelectItem value="year">Year</SelectItem>
            </SelectContent>
          </Select>
        </div>
        <Button className="w-full" onClick={onSearch}>
          Apply Filters
        </Button>
      </CardContent>
    </Card>
  );
};
