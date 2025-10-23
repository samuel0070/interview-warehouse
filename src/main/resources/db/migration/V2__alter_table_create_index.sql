CREATE INDEX idx_categories_parent_id ON categories(parent_id);
CREATE INDEX idx_items_category_id ON items(category_id);
CREATE INDEX idx_variants_item_id ON variants(item_id);
CREATE INDEX idx_variants_sku ON variants(sku);
CREATE INDEX idx_variants_size ON variants(size);
CREATE INDEX idx_variants_color ON variants(color);
CREATE INDEX idx_variants_material ON variants(material);
CREATE INDEX idx_inventory_logs_variant_id ON inventory_logs(variant_id);