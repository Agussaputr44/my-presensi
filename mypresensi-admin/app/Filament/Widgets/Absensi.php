<?php

namespace App\Filament\Widgets;

use App\Models\Attendance;
use Filament\Tables;
use Filament\Widgets\TableWidget as BaseWidget;
use Illuminate\Database\Eloquent\Builder;

class Absensi extends BaseWidget
{
    protected int | string | array $columnSpan = 'full'; // Set to 'full' for full width

    protected function getTableQuery(): Builder
    {
        return Attendance::query()
            ->with('user')
            ->latest();
    }

    protected function getTableColumns(): array
    {
        return [
            
            Tables\Columns\TextColumn::make('user.name')
                ->label('Nama Karyawan')
                ->searchable()
                ->sortable(),

            Tables\Columns\TextColumn::make('created_at')
                ->label('Tanggal')
                ->date('d/m/Y')
                ->sortable(),

                Tables\Columns\TextColumn::make('type')
                ->label('Jenis Kehadiran')
                ->badge()
                ->formatStateUsing(fn (string $state): string => match ($state) {
                    'hadir' => 'Hadir',
                    'izin' => 'Izin',
                    'sakit' => 'Sakit',
                    'alpa' => 'Alpa',
                    default => $state,
                })
                ->color(fn (string $state): string => match ($state) {
                    'hadir' => 'success',
                    'izin' => 'warning',
                    'sakit' => 'danger',
                    'alpa' => 'secondary',
                    default => 'secondary',
                }),
            
            Tables\Columns\TextColumn::make('status')
                ->label('Status Waktu')
                ->badge()
                ->formatStateUsing(fn (string $state): string => match ($state) {
                    'on time' => 'Tepat Waktu',
                    'late' => 'Terlambat',
                    default => $state,
                })
                ->color(fn (string $state): string => match ($state) {
                    'on time' => 'success',
                    'late' => 'danger',
                    default => 'secondary',
                }),

            Tables\Columns\TextColumn::make('reason')
                ->label('Alasan')
                ->wrap()
                ->placeholder('-'),
        ];
    }
}