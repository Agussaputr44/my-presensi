<?php
namespace App\Filament\Resources;

use App\Filament\Resources\AttendancesSummaryResource\Pages;
use App\Models\AttendanceSummary;
use App\Models\User;
use Filament\Forms;
use Filament\Forms\Form;
use Filament\Forms\Components\TextInput;
use Filament\Forms\Components\Select;
use Filament\Resources\Resource;
use Filament\Tables;
use Filament\Tables\Columns\TextColumn;
use Filament\Tables\Table;
use Filament\Tables\Filters\SelectFilter;
use Maatwebsite\Excel\Facades\Excel;
use App\Exports\AttendancesSummaryExport;
use Illuminate\Support\Facades\Log;
use Filament\Tables\Actions\Action;

class AttendancesSummaryResource extends Resource
{
    protected static ?string $model = AttendanceSummary::class;

    protected static ?string $navigationIcon = 'heroicon-o-clipboard-document-check';
    protected static ?string $navigationGroup = 'Kelola Absensi';
    protected static ?string $navigationLabel = 'Rekap Absensi';

    public static function form(Forms\Form $form): Form
    {
        return $form
            ->schema([
                Select::make('user_id')
                    ->label('Pengguna')
                    ->options(User::query()->pluck('name', 'id'))
                    ->searchable()
                    ->required(),

                TextInput::make('month')
                    ->label('Bulan')
                    ->numeric()
                    ->minValue(1)
                    ->maxValue(12)
                    ->required(),

                TextInput::make('year')
                    ->label('Tahun')
                    ->numeric()
                    ->minValue(2000)
                    ->maxValue(date('Y') + 1)
                    ->required(),
            ]);
    }

    public static function table(Tables\Table $table): Table
    {
        return $table
            ->columns([
                TextColumn::make('user.name')
                    ->label('Pengguna')
                    ->searchable()
                    ->sortable(),

                TextColumn::make('month')
                    ->label('Bulan')
                    ->sortable(),

                TextColumn::make('year')
                    ->label('Tahun')
                    ->sortable(),

                TextColumn::make('present_count')
                    ->label('Hadir')
                    ->sortable(),

                TextColumn::make('sick_count')
                    ->label('Sakit')
                    ->sortable(),

                TextColumn::make('leave_count')
                    ->label('Izin')
                    ->sortable(),

                TextColumn::make('late_count')
                    ->label('Terlambat')
                    ->sortable(),

                TextColumn::make('alpa_count')
                    ->label('Alpha')
                    ->sortable(),

                TextColumn::make('created_at')
                    ->label('Dibuat Pada')
                    ->dateTime()
                    ->sortable(),

            ])
            ->filters([
                SelectFilter::make('year')
                    ->label('Tahun')
                    ->options([
                        '2023' => '2023',
                        '2024' => '2024',
                        '2025' => '2025',
                        // Add more years as needed
                    ]),
                SelectFilter::make('month')
                    ->label('Bulan')
                    ->options([
                        '01' => 'Januari',
                        '02' => 'Februari',
                        '03' => 'Maret',
                        '04' => 'April',
                        '05' => 'Mei',
                        '06' => 'Juni',
                        '07' => 'Juli',
                        '08' => 'Agustus',
                        '09' => 'September',
                        '10' => 'Oktober',
                        '11' => 'November',
                        '12' => 'Desember',
                    ]),
            ])
            ->bulkActions([
                Tables\Actions\DeleteBulkAction::make(),
            ])
            ->headerActions([
                Action::make('export')
                    ->label('Export to Excel')
                    ->action(function ($livewire) {
                        // Ambil filter yang diterapkan di tabel
                        $filters = $livewire->tableFilters;

                        // Pastikan filter diteruskan dengan benar
                        $exportFilters = [];
                        if (!empty($filters['year']['value'])) {
                            $exportFilters['year'] = $filters['year']['value'];
                        }
                        if (!empty($filters['month']['value'])) {
                            $exportFilters['month'] = $filters['month']['value'];
                        }


                        return Excel::download(new AttendancesSummaryExport($exportFilters), 'attendances-summary.xlsx');
                    }),
            ]);
    }

    public static function getRelations(): array
    {
        return [
            // Add relations if needed
        ];
    }

    public static function getPages(): array
    {
        return [
            'index' => Pages\ListAttendancesSummaries::route('/'),
            'create' => Pages\CreateAttendancesSummary::route('/create'),
            'edit' => Pages\EditAttendancesSummary::route('/{record}/edit'),
        ];
    }

    // Menghitung summary setelah record dibuat
    public static function afterCreate(Form $form, $record)
    {
        $data = $form->getState();

        $record->calculateSummary($data['user_id'], $data['month'], $data['year']);
    }
}