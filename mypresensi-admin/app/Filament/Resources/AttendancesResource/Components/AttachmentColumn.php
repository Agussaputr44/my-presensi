<?php

namespace App\Filament\Resources\AttendanceResource\Components;

use Filament\Tables\Columns\Column;
use Illuminate\Support\Facades\Storage;

class AttachmentColumn extends Column
{
    protected string $view = 'filament.tables.columns.attachment-column';

    public function getAttachmentUrl(): ?string 
    {
        $path = $this->getState();
        
        if (empty($path)) {
            return null;
        }
        
        return Storage::url($path);
    }

    public function isImage(): bool 
    {
        $path = $this->getState();
        
        if (empty($path)) {
            return false;
        }
        
        $extension = strtolower(pathinfo($path, PATHINFO_EXTENSION));
        return in_array($extension, ['jpg', 'jpeg', 'png', 'gif', 'webp']);
    }

    public function getFileName(): string 
    {
        $path = $this->getState();
        return basename($path);
    }
}